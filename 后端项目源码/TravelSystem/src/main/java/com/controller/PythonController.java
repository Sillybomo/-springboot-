package com.controller;

/**
 * @Author: 薄墨
 * @Description: TODO
 * @DateTime: 2025/9/23 8:19
 **/
import com.utils.PythonUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@CrossOrigin
@RestController
public class PythonController {
    @Value("${file.upload.path}")
    private String path;
    @GetMapping(value = "/getPie")
    public String getPie(){
        System.out.println("-------getPie--------");
        System.out.println("path:"+path);
//根据path，创建File对象
        File file=new File(path);
//判断目录是否存在，如果不存在，则创建目录
        if(!file.exists()){
            file.mkdir();
        }
// 调用工具类执行Python脚本,并将保存路径与图片名称传入进去
        PythonUtils.executePython("attractionTop.py",path,"pie.png");
//返回访问图片的相对路径，不能给出绝对路径
        return path+"pie.png";
    }


    //处理在绘制混合图
    @RequestMapping("/getGrid")
    public String getGrid(@RequestParam(value = "pname", required = false, defaultValue = "") String pname){
        System.out.println("------getGrid------");
        System.out.println("pname:"+pname);
        
        // 如果pname为空，返回默认图片或错误信息
        if(pname == null || pname.trim().isEmpty()) {
            System.out.println("景点名称为空，使用默认值");
            pname = "故宫博物院";
        }
        
        //根据path，创建File对象
        File file=new File(path);
        //判断目录是否存在，如果不存在，则创建目录
        if(!file.exists()){
            file.mkdirs();
        }
        
        // 调用工具类执行Python脚本,并将保存路径与图片名称传入进去
        PythonUtils.executePython("analysis.py",path,"grid.png",pname);
        
        // 返回动态生成的图片路径
        String safePname = pname.replace(" ", "_").replace("/", "_");
        return path + safePname + "_order_chart.png";
    }




    //获取订单量排名前10的景点名称
    @GetMapping("/getTop10Attractions")
    public List<String> getTop10Attractions(){
        System.out.println("-------getTop10Attractions--------");
        try {
            // 调用工具类执行Python脚本获取前10景点名称
            String result = PythonUtils.executePythonForData("attractionTop.py", "get_names");
            if(result != null && !result.trim().isEmpty()) {
                // 解析返回的景点名称，假设用逗号分隔
                String[] attractions = result.split(",");
                List<String> attractionList = new ArrayList<>();
                for(String attraction : attractions) {
                    if(attraction != null && !attraction.trim().isEmpty()) {
                        attractionList.add(attraction.trim());
                    }
                }
                return attractionList;
            }
        } catch (Exception e) {
            System.out.println("获取前10景点失败: " + e.getMessage());
            e.printStackTrace();
        }
        // 返回默认的景点列表
        return Arrays.asList("故宫博物院", "天安门广场", "颐和园", "长城", "天坛", "北海公园", "景山公园", "中山公园", "劳动人民文化宫", "太庙");
    }

    //处理显示图片的请求
    @RequestMapping("/showImages")
    public void show(@RequestParam("path") String path, HttpServletResponse
            response){
        System.out.println("----处理图片显示----");
        System.out.println("path:"+path);
        if(path!=""&&path!=null){
            try {
//根据path指定的文件，创建字节输入流
                InputStream inputStream = new FileInputStream(path);
//获取绑定了客户端的输出流
                ServletOutputStream outputStream=response.getOutputStream();
//将输入流中的数据写出到输出流中
                IOUtils.copy(inputStream,outputStream);
//关闭流
                outputStream.close();
                inputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}