<template>
    <div class="chat-container">
        <!-- 左侧历史记录 -->
        <div class="history-sidebar">
            <div class="history-header">
                <span>历史记录</span>
                <div class="header-controls">
                    <button v-if="currentHistoryIndex !== -1" @click="backToCurrentChat" class="header-btn back-to-current-btn" title="返回当前对话">
                        <i class="fas fa-arrow-left"></i>
                    </button>
                </div>
            </div>
            <div class="history-list">
                <div v-for="(session, index) in historySessions" :key="index" 
                    :class="['history-session', { 'active': currentHistoryIndex === index }]"
                    @click="loadHistory(index)">
                    <div v-for="(msg, msgIndex) in session" :key="msgIndex" class="history-message" :class="msg.role">
                        {{ msg.content.slice(0, 30) }}...
                    </div>
                </div>
            </div>
        </div>
        <!-- 右侧当前对话 -->
        <div class="current-chat">
            <div class="messages" ref="messagesContainer">
                <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.role]">
                    <div class="avatar" :class="msg.role">
                        <i :class="msg.role === 'user' ? 'fas fa-user' : 'fas fa-robot'"></i>
                    </div>
                    <div class="content" :class="{ 
                        'thinking': msg.type === 'thinking',
                        'streaming': msg.type === 'streaming',
                        'result': msg.type === 'result'
                    }">
                        <div v-if="msg.role === 'assistant'" v-html="formatAIContent(msg.content)"></div>
                        <div v-else>{{ msg.content }}</div>
                        <span v-if="msg.isStreaming" class="cursor">|</span>
                    </div>
                </div>
            </div>
            <div class="input-area">
                <div class="input-tools">
                    <button class="tool-btn" title="清空对话" @click="clearCurrentChat">
                        <i class="fas fa-trash"></i>
                    </button>
                    <button class="tool-btn" :title="showThinkProcess ? '隐藏思考过程' : '显示思考过程'" @click="showThinkProcess = !showThinkProcess">
                        <i :class="showThinkProcess ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
                    </button>
                    <div class="speed-control">
                        <span>速度:</span>
                        <select v-model="streamSpeed" class="speed-select">
                            <option value="0.5">慢</option>
                            <option value="1">正常</option>
                            <option value="2">快</option>
                            <option value="4">很快</option>
                        </select>
                    </div>
                </div>
                <input v-model="inputMessage" @keyup.enter="sendMessage" placeholder="输入消息..." class="input-box" />
                <button @click="sendMessage" class="send-btn">
                    <i class="fas fa-paper-plane"></i>
                </button>
            </div>
        </div>
    </div>
</template>
<script>
export default {
    data() {
        return {
            inputMessage: '',
            messages: [],
            historySessions: [], // 存储历史对话
            streamSpeed: 1, // 流式显示速度倍数，1为正常速度
            sessionId: 'session_' + Date.now(), // 当前会话ID
            currentHistoryIndex: -1, // 当前查看的历史记录索引，-1表示当前对话
            latestConversation: [], // 保存最新的对话内容
            latestSessionId: 'session_' + Date.now(), // 保存最新对话的会话ID
            showThinkProcess: true // 是否显示思考过程
        }
    },
    methods: {
        async sendMessage() {
            if (!this.inputMessage.trim()) return
            
            // 如果当前在查看历史记录，切换回最新对话
            if (this.currentHistoryIndex !== -1) {
                // 保存当前历史记录的修改
                this.historySessions[this.currentHistoryIndex] = [...this.messages]
                // 恢复最新对话
                this.messages = [...this.latestConversation]
                this.sessionId = this.latestSessionId
                this.currentHistoryIndex = -1
            }
            
            // 添加用户消息到当前对话
            this.messages.push({
                role: 'user',
                content: this.inputMessage,
                type: 'result'
            })
            
            // 创建新的流式连接
            this.createEventSource(this.inputMessage)
            this.inputMessage = ''
        },
        async createEventSource(question) {
            
            // 初始化AI消息（思考状态）
            const aiMessage = {
                role: 'assistant',
                content: '',
                type: 'thinking',
                isStreaming: false
            }
            this.messages.push(aiMessage)
            
            // 开始思考动画
            this.startThinkingAnimation(aiMessage)
            
            try {
                const response = await fetch(`/api/ai/streamChat?question=${encodeURIComponent(question)}&sessionId=${this.sessionId}`)
                
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`)
                }
                
                const reader = response.body.getReader()
                const decoder = new TextDecoder()
                let buffer = ''
                
                while (true) {
                    const { done, value } = await reader.read()
                    
                    if (done) {
                        // 流结束，停止思考动画，标记为结果
                        this.stopThinkingAnimation(aiMessage)
                        aiMessage.type = 'result'
                        aiMessage.isStreaming = false
                        
                        // 保存最新对话状态
                        this.updateLatestConversation()
                        break
                    }
                    
                    // 解码并处理内容
                    const chunk = decoder.decode(value, { stream: true })
                    buffer += chunk
                    
                    // 停止思考动画，开始流式显示
                    if (aiMessage.type === 'thinking') {
                        this.stopThinkingAnimation(aiMessage)
                        aiMessage.type = 'streaming'
                        aiMessage.content = ''
                        aiMessage.isStreaming = true
                    }
                    
                    // 流式显示效果，模拟打字机
                    await this.streamText(aiMessage, buffer)
                }
            } catch (error) {
                console.error('流式请求错误:', error)
                this.stopThinkingAnimation(aiMessage)
                aiMessage.content = '抱歉，连接出现问题，请稍后重试。'
                aiMessage.type = 'result'
                aiMessage.isStreaming = false
                
                // 即使出错也保存最新对话状态
                this.updateLatestConversation()
            }
        },
        
        // 开始思考动画
        startThinkingAnimation(message) {
            const dots = ['', '.', '..', '...']
            let dotIndex = 0
            
            message.thinkingInterval = setInterval(() => {
                if (message.type === 'thinking') {
                    message.content = 'AI正在思考中' + dots[dotIndex]
                    dotIndex = (dotIndex + 1) % dots.length
                    this.scrollToBottom()
                }
            }, 500)
        },
        
        // 停止思考动画
        stopThinkingAnimation(message) {
            if (message.thinkingInterval) {
                clearInterval(message.thinkingInterval)
                message.thinkingInterval = null
            }
        },
        
        // 流式文本显示，模拟打字机效果
        async streamText(message, fullText) {
            // 计算需要显示的新内容
            const currentLength = message.content.length
            const newContent = fullText.slice(currentLength)
            
            if (newContent.length === 0) return
            
            // 按字符逐个显示，模拟打字机效果
            for (let i = 0; i < newContent.length; i++) {
                message.content += newContent[i]
                this.scrollToBottom()
                
                // 根据字符类型调整显示速度
                const char = newContent[i]
                let delay = 30 // 默认延迟
                
                if (char === ' ') {
                    delay = 20 // 空格稍快
                } else if (char === '\n') {
                    delay = 50 // 换行稍慢
                } else if (char.match(/[，。！？；：]/)) {
                    delay = 100 // 中文标点稍慢
                } else if (char.match(/[,.!?;:]/)) {
                    delay = 80 // 英文标点稍慢
                } else if (char.match(/[a-zA-Z]/)) {
                    delay = 25 // 英文字母稍快
                } else if (char.match(/[\u4e00-\u9fa5]/)) {
                    delay = 40 // 中文字符
                }
                
                // 应用速度倍数
                delay = Math.max(1, delay / this.streamSpeed)
                
                await new Promise(resolve => setTimeout(resolve, delay))
            }
        },

        // 将 AI 内容格式化为"思考 + 答复"两段，思考淡化显示
        formatAIContent(content) {
            if (!content) return ''

            // 提取 <think>...</think>
            const thinkRegex = /<think>([\s\S]*?)<\/think>/i
            const match = content.match(thinkRegex)
            let think = ''
            let answer = content

            if (match) {
                think = match[1]
                answer = content.replace(thinkRegex, '').trim()
            }

            const safeThink = this.escapeHtml(think).trim()
            const safeAnswer = this.escapeHtml(answer)

            // 思考部分：根据用户设置决定是否显示
            const thinkBlock = (safeThink && this.showThinkProcess)
                ? `<div class="ai-think">
                     <div class="ai-think-title" onclick="this.parentElement.classList.toggle('collapsed')">
                         <i class="fas fa-brain"></i>
                         <span>AI 思考过程</span>
                         <i class="fas fa-chevron-down toggle-icon"></i>
                     </div>
                     <div class="ai-think-content">${safeThink.replace(/\n/g, '<br/>')}</div>
                   </div>`
                : ''

            // 回答部分：正常突出显示
            const answerBlock = safeAnswer
                ? `<div class="ai-answer">${safeAnswer.replace(/\n/g, '<br/>')}</div>`
                : ''

            // 如果思考过程被隐藏但确实存在，添加一个小提示
            const hiddenThinkHint = (!this.showThinkProcess && safeThink)
                ? `<div class="think-hidden-hint">AI 思考过程已隐藏 <i class="fas fa-eye-slash"></i></div>`
                : ''

            return `${thinkBlock}${answerBlock}${hiddenThinkHint}`
        },

        // HTML 转义，避免 XSS
        escapeHtml(text) {
            const div = document.createElement('div')
            div.textContent = text || ''
            return div.innerHTML
        },

        // 更新最新对话状态
        updateLatestConversation() {
            // 只在当前对话模式下更新（不是查看历史记录时）
            if (this.currentHistoryIndex === -1) {
                this.latestConversation = [...this.messages]
                this.latestSessionId = this.sessionId
            }
        },
        
        // 清空当前对话
        async clearCurrentChat() {
            // 如果当前有对话内容，保存到历史记录
            if (this.messages.length > 0) {
                this.historySessions.push([...this.messages])
            }
            
            // 清空后端会话历史
            await this.clearBackendSession()
            
            // 清空所有对话状态
            this.messages = []
            this.latestConversation = []
            this.sessionId = 'session_' + Date.now()
            this.latestSessionId = this.sessionId
            this.currentHistoryIndex = -1
        },
        
        // 清空后端会话历史（内部方法）
        async clearBackendSession() {
            try {
                await fetch(`/api/ai/clearSession?sessionId=${this.sessionId}`)
            } catch (error) {
                console.error('清空后端会话历史失败:', error)
            }
        },
        
        // 加载历史记录
        loadHistory(index) {
            // 如果当前在对话模式且有内容，保存为最新对话
            if (this.currentHistoryIndex === -1 && this.messages.length > 0) {
                this.updateLatestConversation()
                
                // 检查是否需要保存到历史记录（避免重复保存）
                const currentSession = JSON.stringify(this.messages)
                const existingSession = this.historySessions.find(session => 
                    JSON.stringify(session) === currentSession
                )
                
                if (!existingSession) {
                    this.historySessions.push([...this.messages])
                }
            }
            
            // 清空当前会话的后端历史
            this.clearBackendSession()
            
            // 加载选中的历史记录
            this.messages = [...this.historySessions[index]]
            
            // 设置当前查看的历史记录索引
            this.currentHistoryIndex = index
            
            // 生成新的会话ID（因为这是一个新的会话上下文）
            this.sessionId = 'session_' + Date.now()
            
            this.scrollToBottom()
        },
        
        // 返回当前对话
        backToCurrentChat() {
            // 如果当前查看的是历史记录，先保存当前修改（如果有的话）
            if (this.currentHistoryIndex !== -1) {
                this.historySessions[this.currentHistoryIndex] = [...this.messages]
            }
            
            // 清空后端会话历史
            this.clearBackendSession()
            
            // 恢复最新对话
            this.messages = [...this.latestConversation]
            this.sessionId = this.latestSessionId || 'session_' + Date.now()
            
            // 重置到当前对话模式
            this.currentHistoryIndex = -1
            
            this.scrollToBottom()
        },
        
        scrollToBottom() {
            this.$nextTick(() => {
                const container = this.$refs.messagesContainer
                container.scrollTop = container.scrollHeight
            })
        }
    }
}
</script>
<style scoped>
/* 引入Font Awesome图标库 */
@import url('https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css');

/* 全局样式重置与基础配置 */
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

.chat-container {
    display: flex;
    max-width: 1400px;
    margin: 0 auto;
    height: 100vh;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.05);
    overflow: hidden;
    background-color: #fff;
}

/* 左侧历史记录样式 - 优化紧凑度与交互 */
.history-sidebar {
    width: 300px;
    background: #ffffff;
    border-right: 1px solid #f0f0f0;
    display: flex;
    flex-direction: column;
    transition: all 0.3s ease;
}

  .history-header {
      padding: 16px 20px;
      font-weight: 600;
      font-size: 15px;
      color: #333;
      border-bottom: 1px solid #f0f0f0;
      background: #fafafa;
      display: flex;
      justify-content: space-between;
      align-items: center;
  }

  .header-controls {
      display: flex;
      gap: 8px;
  }

  .header-btn {
      width: 32px;
      height: 32px;
      border-radius: 6px;
      border: none;
      background: #e8f0fe;
      color: #4285f4;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
      transition: all 0.2s ease;
  }

  .header-btn:hover {
      background: #d2e3fc;
      transform: scale(1.05);
  }
  
  .back-to-current-btn {
      width: 28px;
      height: 28px;
      border-radius: 50%;
      border: none;
      background: #4285f4;
      color: white;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 12px;
      transition: all 0.2s ease;
  }
  
  .back-to-current-btn:hover {
      background: #3367d6;
      transform: scale(1.05);
  }

.history-list {
    flex: 1;
    overflow-y: auto;
    padding: 8px;
}

.history-session {
    padding: 10px 12px;
    margin-bottom: 6px;
    background: white;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s ease;
    border: 1px solid transparent;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
}

.history-session:hover {
    background: #f8f9fa;
    transform: translateY(-1px);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.history-session.active {
    border-color: #d2e3fc;
    background: #e8f0fe;
}

.history-message {
    font-size: 12px;
    margin: 3px 0;
    padding: 2px 6px;
    border-radius: 4px;
    line-height: 1.3;
}

.history-message.user {
    background: #e3f2fd;
    color: #1967d2;
    margin-left: 12px;
}

.history-message.assistant {
    background: #f5f5f5;
    color: #5f6368;
    margin-right: 12px;
}

/* 右侧当前对话样式 - 核心优化：减小对话框高度+AI思考状态分离 */
.current-chat {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #f9fafb;
}

/* 优化后：减小上下内边距（比如从20px→10px），左右内边距可保留 */
.messages {
    flex: 1;
    padding: 10px 20px; /* 上下10px，左右20px，垂直高度直接减少20px（10px*2） */
    overflow-y: auto; /* 必须保留，确保消息过多时能滚动 */
    background-color: #f9fafb;
    background-image: radial-gradient(circle at 1px 1px, rgba(0, 0, 0, 0.02) 1px, transparent 0);
    background-size: 20px 20px;
}

/* 消息容器 - 优化间距 */
.message {
    margin: 8px 0;
    display: flex;
    align-items: flex-start;
    max-width: 90%;
}

.message.user {
    margin-left: auto;
    flex-direction: row-reverse;
}

.message.assistant {
    margin-right: auto;
}

/* 头像 - 颜色协调 */
.avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 8px;
    flex-shrink: 0;
}

.avatar.user {
    background-color: #4285f4;
    color: white;
}

.avatar.assistant {
    background-color: #e8f0fe;
    color: #4285f4;
}

/* 对话框核心样式 - 减小高度：压缩padding+字体 */
.content {
    max-width: 85%;
    padding: 10px 16px; /* 原14px 18px → 减小垂直padding */
    border-radius: 16px; /* 原18px → 更紧凑 */
    line-height: 1.5; /* 原1.6 → 减小行高 */
    font-size: 14px; /* 原15px → 缩小字体 */
    position: relative;
    word-wrap: break-word;
    transition: all 0.2s ease;
}

/* 用户对话框 - 颜色优化 */
.user .content {
    background-color: #4285f4;
    color: white;
    border-bottom-right-radius: 4px;
    box-shadow: 0 1px 3px rgba(66, 133, 244, 0.15);
}

/* AI正常对话框 - 轻量化阴影 */
.assistant .content {
    background-color: white;
    color: #333;
    border-bottom-left-radius: 4px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
}

/* AI思考状态 - 淡化+分离核心样式 */
.content.thinking {
    background-color: #f8f8f8; /* 浅灰背景 → 分离正常消息 */
    border: 1px solid #eee; /* 细边框 → 强化分离感 */
    color: #b0b0b0; /* 浅灰文字 → 淡化效果 */
    font-size: 13px; /* 更小字体 → 进一步淡化 */
    font-style: italic;
    animation: pulse 1.8s ease-in-out infinite; /* 更柔和的动画 */
}

/* 流式输出状态 - 保持清晰 */
.content.streaming {
    color: #333;
}

/* 结果状态 - 正常显示 */
.content.result {
    color: #333;
}

/* AI思考过程样式 - 淡化显示，支持折叠 */
.ai-think {
    margin-bottom: 12px;
    padding: 8px 12px;
    background: #f9f9f9;
    border: 1px solid #e5e7eb;
    border-radius: 8px;
    border-left: 3px solid #9ca3af;
    transition: all 0.2s ease;
}

.ai-think.collapsed .ai-think-content {
    display: none;
}

.ai-think.collapsed .toggle-icon {
    transform: rotate(-90deg);
}

.ai-think-title {
    display: flex;
    align-items: center;
    gap: 6px;
    color: #9ca3af;
    font-size: 12px;
    margin-bottom: 6px;
    font-weight: 500;
    cursor: pointer;
    user-select: none;
}

.ai-think-title:hover {
    color: #6b7280;
}

.ai-think-title i {
    color: #9ca3af;
    font-size: 12px;
}

.toggle-icon {
    margin-left: auto;
    transition: transform 0.2s ease;
    color: #9ca3af;
}

.ai-think-content {
    color: #6b7280;
    font-size: 13px;
    line-height: 1.5;
    white-space: pre-wrap;
    font-style: italic;
    animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
    from { opacity: 0; transform: translateY(-5px); }
    to { opacity: 1; transform: translateY(0); }
}

/* AI回答主体样式 - 突出显示 */
.ai-answer {
    color: #1f2937;
    font-size: 15px;
    line-height: 1.6;
    font-weight: 400;
}

/* 思考过程隐藏提示 */
.think-hidden-hint {
    margin-top: 8px;
    padding: 4px 8px;
    background: #f0f0f0;
    border-radius: 4px;
    font-size: 11px;
    color: #9ca3af;
    display: flex;
    align-items: center;
    gap: 4px;
    border: 1px solid #e5e7eb;
}

.think-hidden-hint i {
    font-size: 10px;
}

/* 打字机光标 - 优化颜色匹配 */
.cursor {
    animation: blink 1s infinite;
    color: inherit;
    font-weight: normal;
    margin-left: 2px;
}

/* 动画优化 - 更柔和的过渡 */
@keyframes blink {
    0%, 50% { opacity: 1; }
    51%, 100% { opacity: 0; }
}

@keyframes pulse {
    0%, 100% { opacity: 1; }
    50% { opacity: 0.8; } /* 原0.7 → 更柔和 */
}

/* 滚动条 - 更细腻的样式 */
::-webkit-scrollbar {
    width: 5px;
    height: 5px;
}

::-webkit-scrollbar-track {
    background: transparent;
}

::-webkit-scrollbar-thumb {
    background: #d1d5db;
    border-radius: 3px;
}

::-webkit-scrollbar-thumb:hover {
    background: #94a3b8; /* 更深的hover色 → 提升交互感 */
}

/* 输入区域 - 优化高度与交互 */
.input-area {
    display: flex;
    padding: 12px 20px;
    background-color: white;
    border-top: 1px solid #f0f0f0;
    align-items: center;
}

.input-tools {
    display: flex;
    align-items: center;
    margin-right: 10px;
    gap: 8px; /* 添加按钮之间的间距 */
}

/* 工具按钮 - 颜色协调 */
.tool-btn {
    width: 34px;
    height: 34px;
    border-radius: 50%;
    border: none;
    background: #f5f5f5;
    color: #666;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.2s ease;
}

.tool-btn:hover {
    background: #e8f0fe;
    color: #4285f4; /* 主题色呼应 */
}

/* 速度控制 - 优化字体与间距 */
.speed-control {
    display: flex;
    align-items: center;
    margin-left: 10px;
    font-size: 12px;
    color: #666;
}

.speed-select {
    margin-left: 5px;
    padding: 3px 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 12px;
    background: white;
    outline: none;
    transition: border-color 0.2s;
}

.speed-select:focus {
    border-color: #4285f4; /* 主题色聚焦 */
}

/* 输入框 - 减小高度 */
.input-box {
    flex: 1;
    padding: 12px 16px; /* 原14px 18px → 压缩 */
    border: 1px solid #e0e0e0;
    border-radius: 24px;
    margin-right: 10px;
    outline: none;
    font-size: 14px;
    transition: all 0.2s ease;
    resize: none;
    height: 48px; /* 原52px → 减小高度 */
    max-height: 100px; /* 原120px → 同步压缩 */
    overflow-y: auto;
}

.input-box:focus {
    border-color: #4285f4;
    box-shadow: 0 0 0 3px rgba(66, 133, 244, 0.15); /* 更细腻的聚焦阴影 */
}

/* 发送按钮 - 优化交互 */
.send-btn {
    width: 48px;
    height: 48px; /* 原52px → 匹配输入框高度 */
    background-color: #4285f4;
    color: white;
    border: none;
    border-radius: 50%;
    cursor: pointer;
    transition: all 0.3s;
    display: flex;
    align-items: center;
    justify-content: center;
}

.send-btn:hover {
    background-color: #3367d6; /* 更深的hover色 */
    transform: scale(1.05);
}

.send-btn:active {
    transform: scale(0.98);
}

/* 响应式适配 - 同步压缩样式 */
@media (max-width: 768px) {
    .history-sidebar {
        width: 70px;
    }
    
    .history-header, .history-message {
        display: none;
    }
    
    .history-session {
        padding: 8px;
        text-align: center;
    }
    
    /* 移动端对话框进一步压缩 */
    .content {
        max-width: 80%;
        font-size: 13px;
        padding: 8px 12px;
        border-radius: 12px;
    }
    
    .content.thinking {
        font-size: 12px;
    }
    
    /* 移动端输入框压缩 */
    .input-box {
        height: 42px;
        padding: 10px 14px;
    }
    
    .send-btn {
        width: 42px;
        height: 42px;
    }
}
</style>