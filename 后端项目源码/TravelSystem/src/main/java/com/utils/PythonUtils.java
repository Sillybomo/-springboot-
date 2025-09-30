package com.utils;

/**
 * @Author: 薄墨
 * @Description: TODO
 * @DateTime: 2025/9/22 21:42
 **/
import org.springframework.util.ResourceUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
//工具类：用于执行Python脚本并处理输出。
public class PythonUtils {
/**
 * 执行Python脚本并处理输出。
 * @param args 传递给Python脚本程序的参数（可选）
 */
//注意：args的第一个值必须是保存图片的目录，第二个是保存图片的名称，第三个及以后参数
    public static void executePython(String fileName,String... args) {
        try {
// 获取resources目录下指定文件的File对象
            File file = ResourceUtils.getFile("classpath:python/"+fileName);
// 获取python脚本文件绝对路径
            String filePath = file.getAbsolutePath();
            System.out.println("filePath:"+filePath);
//获取python解释器路径
            String pythonPath = "D:/Software/Python/python.exe";
// 创建ProcessBuilder对象，用于执行外部python程序
//第一个参数表示python的解释器路径
//第二个参数表示需要执行的python文件的绝对路径
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath,
                    filePath);
            
            // 设置环境变量，确保Python输出使用UTF-8编码
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
// 添加参数到需要执行的python脚本程序中
            if (args != null) {
                for (String arg : args) {
                    System.out.println("需要传入python程序的参数:"+arg);
                    processBuilder.command().add(arg);
                }
            }
// 启动外部进程，执行脚本，返回Process 对象，用于管理该进程的输入/输出流、
            Process process= processBuilder.start();
//2.获取python程序中的print的输出结果，如果没有print输出，则下面代码可以省

//通过Process对象获取字节输入流
            InputStream inputStream=process.getInputStream();
//将字节流转换成字符流，指定UTF-8编码
            InputStreamReader reader=new InputStreamReader(inputStream, "UTF-8");
//将字节输入流转换成缓冲流
            BufferedReader bufferedReader=new BufferedReader(reader);
            String line=null; //用于保存python执行的每一行的输出
// 获取命令行输出
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }catch (Exception e){
            System.out.println("执行python脚本失败");
            e.printStackTrace();
        }
    }

    /**
     * 执行Python脚本并返回数据结果
     * @param fileName Python脚本文件名
     * @param mode 执行模式
     * @param args 传递给Python脚本程序的参数
     * @return 返回Python脚本的输出结果
     */
    public static String executePythonForData(String fileName, String mode, String... args) {
        try {
            // 获取resources目录下指定文件的File对象
            File file = ResourceUtils.getFile("classpath:python/"+fileName);
            // 获取python脚本文件绝对路径
            String filePath = file.getAbsolutePath();
            System.out.println("filePath:"+filePath);
            //获取python解释器路径
            String pythonPath = "D:/Software/Python/python.exe";
            // 创建ProcessBuilder对象，用于执行外部python程序
            ProcessBuilder processBuilder = new ProcessBuilder(pythonPath, filePath);
            
            // 设置环境变量，确保Python输出使用UTF-8编码
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
            
            // 添加模式参数
            processBuilder.command().add(mode);
            
            // 添加其他参数到需要执行的python脚本程序中
            if (args != null) {
                for (String arg : args) {
                    System.out.println("需要传入python程序的参数:"+arg);
                    processBuilder.command().add(arg);
                }
            }
            
            // 启动外部进程，执行脚本
            Process process = processBuilder.start();
            
            // 获取python程序中的print的输出结果
            InputStream inputStream = process.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            StringBuilder result = new StringBuilder();
            String line = null;
            // 获取命令行输出
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                result.append(line).append("\n");
            }
            
            // 等待进程完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return result.toString().trim();
            } else {
                System.out.println("Python脚本执行失败，退出码: " + exitCode);
                return null;
            }
            
        } catch (Exception e) {
            System.out.println("执行python脚本失败");
            e.printStackTrace();
            return null;
        }
    }
}
