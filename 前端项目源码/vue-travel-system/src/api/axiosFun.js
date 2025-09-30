import axios from 'axios';

// 登录请求方法
const loginreq = (method, url, params) => {
    return axios({
        method: method,
        url: url,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        data: params,
        traditional: true,
        transformRequest: [
            function(data) {
                let ret = ''
                for (let it in data) {
                    ret +=
                        encodeURIComponent(it) +
                        '=' +
                        encodeURIComponent(data[it]) +
                        '&'
                }
                return ret
            }
        ]
    }).then(res => res.data);
};

// 通用公用方法
const req = (method, url, params) => {
    const config = {
        method: method,
        url: url,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            token: localStorage.getItem('logintoken')
        },
        traditional: true
    };
    
    // 根据请求方法决定参数位置
    if (method.toLowerCase() === 'get') {
        config.params = params;
    } else {
        config.data = params;
        config.transformRequest = [
            function(data) {
                let ret = ''
                for (let it in data) {
                    ret +=
                        encodeURIComponent(it) +
                        '=' +
                        encodeURIComponent(data[it]) +
                        '&'
                }
                return ret
            }
        ];
    }
    
    return axios(config).then(res => res.data);
};

// JSON请求方法（用于需要发送JSON数据的接口）
const jsonReq = (method, url, params) => {
    return axios({
        method: method,
        url: url,
        headers: {
            'Content-Type': 'application/json',
        },
        data: params
    }).then(res => res.data);
};

export {
    loginreq,
    req,
    jsonReq
}