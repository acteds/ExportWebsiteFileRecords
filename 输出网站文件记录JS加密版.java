package 网页;
// javac -cp .;fastjson-1.2.75.jar -encoding UTF-8 -d . 输出网站文件记录JS加密版.java
// java -Dfile.encoding=UTF-8 -cp .;fastjson-1.2.75.jar 网页.输出网站文件记录JS加密版
// java -cp .;fastjson-1.2.75.jar 网页.输出网站文件记录JS加密版
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用以输出网站文件记录
 * @author aotmd
 * @version 1.0.4
 * @date 2021/3/7 22:50
 */
public class 输出网站文件记录JS加密版 {
    /** 主路径 */
    private String index;
    /** 编码 */
    private String coding;
    /** 待输出缓冲区*/
    private StringBuffer sb=new StringBuffer();
    /** 待输出缓冲区*/
    public StringBuffer erreo=new StringBuffer();
    /** 文件计数*/
    private long n=0;
    public String getCoding() { return coding; }public void setCoding(String coding) { this.coding = coding; }
    public String getIndex() { return index; }public void setIndex(String index) { this.index = index; }
    public StringBuffer getSb() { return sb; }public void setSb(String s) { this.sb.append(s); }
    public long getN() { return n; }public void setN(long n) { this.n = n; }
    public void topInsetSb(String s){sb.insert(0,s);}

    public static void main (String[]args)  {
        输出网站文件记录JS加密版 notes=new 输出网站文件记录JS加密版();

        /*初始化*/
        notes.setIndex("https://www.galgame.cyou");
        notes.setCoding("UTF-8");
        Data2 index=new Data2("","0B");
        /*打印头*/
        notes.setSb("网站2");
        /*开始获取文件记录*/
        char[] empty=new char[200];
        Arrays.fill(empty, '1');
        notes.cycle(index,1,empty);

        /*文件数量*/
        notes.topInsetSb("文件数量:"+notes.getN()+"\n\n");
        System.out.print("文件数量:"+notes.getN()+"\n");

        /*获取当前时间*/
        String date = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        notes.topInsetSb("记录时间:"+date+"\n");
        System.out.print("记录时间:"+date+"\n");

        /*输出文件记录到控制台*/
        System.out.println(notes.getSb());

        /*输出错误日志*/
        System.out.println(notes.erreo);

        /*输出文件记录到文件*/
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH;mm;ss");
        String time=" "+df.format(new Date());
        BufferedWriter bw;
        try {
            File file=new File("网站2索引");
            if (!file.exists())file.mkdirs();
            bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("网站2索引/网站2索引"+time+".txt"), StandardCharsets.UTF_8));
            bw.write(notes.getSb().toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*输出文件地址*/
        System.out.print("网站2索引"+time+".txt");
    }
    /**
     * 循环
     * @param Data2 参数
     * @param count 目录级别
     * @param empty 前缀控制
     */
    private void cycle(Data2 Data2, int count, char[] empty) {
        Data2.addPath("/"+Data2.getName());
        JSONArray array = this.httpURLGETCase(Data2.getPath());
        if (array == null) {
            /*输出名称,该目录下的目录与文件数量和,目录大小*/
            sb.append(String.format("%s [%d] [%s]", Data2.getName(), 0, Data2.getSize())).append('\n');
            return;
        }
        List<Data2> list = array.toJavaList(Data2.class);
        /*输出名称,该目录下的目录与文件数量和,目录大小*/
        sb.append(String.format("%s [%d] [%s]", Data2.getName(), list.size(),Data2.getSize())).append('\n');
        /*排序,文件在前*/
        Collections.sort(list);
        for (int i = 0; i < list.size(); i++) {
            Data2 temp = list.get(i);
            if (temp.isIs_folder()) {/*如果是文件夹*/
                empty[count - 1] = '1';
                /*如果上一个是文件那么空一行*/
                if (i > 0 && !list.get(i - 1).isIs_folder()) {
                    sb.append(printFilePrefix(empty, count).replaceAll("\\s+$","")).append('\n');
                }
                /*输出前缀*/
                sb.append(printFilePrefix(empty,count - 1));
                /*如果是最后一个文件夹*/
                if (i >= list.size() - 1) {
                    sb.append("└─");
                    empty[count - 1] = '0';
                } else {
                    sb.append("├─");
                }
                temp.addPath(Data2.getPath());
                cycle(temp, count + 1, empty);
            } else {/*如果是文件*/
                n++;
                sb.append(printFilePrefix(empty,count - 1));
                /*如果该目录下没有文件夹*/
                if (!list.get(list.size() - 1).isIs_folder()) {
                    sb.append("    ");
                } else {
                    sb.append("│  ");
                }
                /*输出名称,大小*/
                sb.append(String.format("%s \t [%s]", temp.getName(), temp.getSize())).append('\n');
                /*显示下载链接*/
//                sb2.append(String.format("%s@%s@%s", temp.getName(), temp.getSize(),temp.getDownload_url())).append('\n');
            }
            /*如果是最后一个*/
            if (i == list.size() - 1) {
                sb.append(printFilePrefix(empty,count - 1).replaceAll("\\s+$","")).append('\n');
            }
        }
    }
    /**
     * 获取网址参数
     * @param url 网址
     * @param parameter 参数名称
     * @return 字符串
     */
    public String getParameters(String url,String parameter){
        System.out.println(getParameters("getpath?path=/", "path"));
        url=url.substring(url.indexOf("?")+1);
        Matcher m1 = Pattern.compile("(^|&)" + parameter + "=([^&]*)(&|$)").matcher(url);
        if (m1.find()) {
            System.out.println(m1.group(1));
            try {
                return URLDecoder.decode(m1.group(2),coding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 添加文件前缀
     * @param empty 空位控制符
     * @param n 减少输出数量
     */
    private String printFilePrefix(char[] empty,int n){
        StringBuilder s= new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = empty[i];
            if (c == '0') {
                s.append("    ");
            } else if (c == '1') {
                s.append("│  ");
            }
        }
        return s.toString();
    }
    /**
     * 转换文件夹大小
     * @param size 文件大小
     * @return 字符串
     */
    private String formatFileSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString;
        String wrongSize = "0B";
        if (size == 0) {
            return wrongSize;
        }
        if (size < 1024) {
            fileSizeString = df.format((double) size) + "B";
        } else if (size < 1048576) {
            fileSizeString = df.format((double) size / 1024) + "KB";
        } else if (size < 1073741824) {
            fileSizeString = df.format((double) size / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) size / 1073741824) + "GB";
        }
        return fileSizeString;
    }
    /**
     * 转换汉字为url
     * @param url 转换前url
     * @return 转换后url
     */
    private String convertChinese(String url) {
        StringBuilder resultURL = new StringBuilder();
        //遍历字符串
        for (int i = 0; i < url.length(); i++) {
            char original = url.charAt(i);
            //只对汉字处理,只要编码在\u4e00到\u9fa5之间的都是汉字
            if (String.valueOf(original).matches("[\u4e00-\u9fa5：]")) {
                String afterConversion = URLEncoder.encode(original+"", StandardCharsets.UTF_8);
                resultURL.append(afterConversion);
            }else {
                resultURL.append(original);
            }
        }
        return resultURL.toString();
    }
    /**
     * 接口内容获取
     *
     * @param methodUrl 部分网址
     * @return JSON对象
     */
    private JSONArray httpURLGETCase(String methodUrl) {
        /*汉字转码,当虚拟机没有 -Dfile.encoding=UTF-8 参数时url出现中文会产生错误*/
        methodUrl = convertChinese(methodUrl);
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String line;
        try {
            URL url = new URL(index + methodUrl);
            connection = (HttpURLConnection) url.openConnection();// 根据URL生成HttpURLConnection
            connection.setRequestMethod("GET");// 默认GET请求
            connection.connect();// 建立TCP连接
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), coding));// 发送http请求
                StringBuilder result = new StringBuilder();
                // 循环读取流
                while ((line = reader.readLine()) != null) {
                    result.append(line).append(System.getProperty("line.separator"));// "\n"
                    if (line.contains("=")) {
                        String temp = line.split("=")[0];
                        /*取到满足条件的JS变量*/
                        if (temp.contains("RawData")) {
                            /*转换成合格的base64后解密*/
                            String t = line.split("=")[1].replaceAll("\"", "").trim();
                            t = t.replaceAll(";", "").replaceAll("\"","");
                            t =t.replaceAll("\\\\x2b","+").replaceAll("\\\\x2f","/");
                            t=new String(base64Decryption(t));
                            /*对json数据进行格式化处理*/
                            t =t.replaceAll("@time","time");
                            t =t.replaceAll("@type","type");
                            t =t.replaceAll("\"type\":\"file\"","\"is_folder\":false");
                            t =t.replaceAll("\"type\":\"folder\"","\"is_folder\":true");
                            return JSONObject.parseArray(t);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }
        return null;
    }
    /**
     * base64解码
     *
     * @param a 解码对象
     * @return 解码结果
     */
    public byte[] base64Decryption(String a) {
        return Base64.getDecoder().decode(a);
    }
}

/**
 * json数据类
 */
class Data2 implements Comparable<Data2>  {
    private String name,size;
    private boolean  is_folder;
    /** 路径 */
    private String path="";

    public Data2(String name,String size) {
        this.name = name;
        this.size = size;
    }

    public Data2() {
    }

    public String getPath() {
        return path;
    }

    public void addPath(String path) {
        this.path += path;
    }

    @Override
    public String toString() {
        return "Data2{" +
                "name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", is_folder=" + is_folder +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        //前端数据+号未转码,而URLDecoder.decode会将之替换为空格,因此手动转码
        name=name.replaceAll("\\+","%2B");
        this.name = URLDecoder.decode(name, StandardCharsets.UTF_8);
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size.replaceAll("\\s","");
    }

    public boolean isIs_folder() {
        return is_folder;
    }

    public void setIs_folder(boolean is_folder) {
        this.is_folder = is_folder;
    }
    /** 排序方法 @param Data2 另一个对象 @return 排序结果 */
    @Override
    public int compareTo(Data2 Data2) {
        int a=this.is_folder?1:0;
        int b=Data2.is_folder?1:0;
        /*文件在文件夹前面*/
        int result=a-b;
        /*相等则比较文件名称,A-Z然后中文排序,升序*/
        Collator collator = Collator.getInstance(java.util.Locale.CHINA);
        if (a-b==0){
            result=collator.compare(this.getName(),Data2.getName());
        }
        return result;
    }
}