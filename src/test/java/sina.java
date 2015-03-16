import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lewin on 15-3-16.
 */
public class sina {
    public static void main(String[] args) throws IOException {
        //第一次请求
        Connection con = Jsoup.connect("http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3A%2F%2Fweibo.cn%2F%3Ffrom%3Dhome&backTitle=%CE%A2%B2%A9&vt=");
        //配置模拟浏览器
        con.header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
        Connection.Response rs = con.execute();//获取响应
        Document loginDoc = Jsoup.parse(rs.body());//转换成Dom树
        List<Element> et = loginDoc.select("form");
        Map<String,String>  loginDatas = new HashMap<String, String>();
        for (Element e:et.get(0).getAllElements())  {
            if (e.attr("name").equals("mobile"))  {
                loginDatas.put("mobile","18717541842");
            }
            if (e.attr("type").equals("password"))  {
                loginDatas.put(e.attr("name"),"l123321");
            }
            loginDatas.put("remember","on");
            loginDatas.put("backURL","http%3A%2F%2Fweibo.cn%2F");
            loginDatas.put("backTitle","微博");
            loginDatas.put("tryCount","");
            if (e.attr("name").equals("vk"))  {
                loginDatas.put("vk",e.attr("value"));
            }
            loginDatas.put("submit","登录");
        }

        //第二次请求
        Connection connection = Jsoup.connect("http://login.weibo.cn/login/?ns=1&revalid=2&backURL=http%3A%2F%2Fweibo.cn%2F%3Ffrom%3Dhome&backTitle=%CE%A2%B2%A9&vt=");
        connection.header("User-Agent","Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36");
        Connection.Response response = connection.method(Connection.Method.POST).data(loginDatas).execute();

    //抓取微博
        Document targetDoc = Jsoup.connect("http://weibo.cn/u/1876112841?vt=4").cookies(response.cookies())
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36")
                .get();
        //定义微博人的ID
        String weiBoName = null;
        //抓取所有的ctt标签 也就是微博内容标签
        Elements sinaContente = targetDoc.getElementsByClass("ctt");
        String weiBoNameTemp = sinaContente.first().toString();//第一个是用户的ID 获得用户的ID
        weiBoName = weiBoNameTemp.substring(weiBoNameTemp.indexOf(">") + 1, weiBoNameTemp.indexOf("<img"));  //进行字符串的处理
        if (weiBoName.length() < 30) {
            System.out.println(weiBoName);
        }
        //创建文件
        try {
            File weiBoFile = new File(weiBoName);
            if (!weiBoFile.exists())  {
                weiBoFile.createNewFile();
            }
            //创建一个BufferReader对象
            BufferedWriter inputIntoFile = new BufferedWriter(new FileWriter(weiBoFile));
            inputIntoFile.write(weiBoName+"\n");
            //检查网页是否有下一页
            Elements checkNext = targetDoc.select("form");
            String checkTemp = checkNext.text();
            while (checkTemp.contains("下页")) {
                int rest = 0;
                //将此页里面的微博内容和时间抓取
                sinaContente = targetDoc.getElementsByClass("c");
                for (Element c : sinaContente) {
                    Element ctt = c.getElementsByClass("ctt").first();
                    Element ct = c.getElementsByClass("ct").first();
                    if (ctt != null && ct != null) {
                        System.out.println(ctt.text());
                        inputIntoFile.write(ctt.text()+"\n");
                        System.out.println(ct.text());
                        inputIntoFile.write(ct.text()+"\n\n");
                    }

                }
                //抓取到下一页的URL
                Elements temp = checkNext.select("a");
                String nextURL = "http://weibo.cn"+ temp.attr("href");
                //进入下一页
                targetDoc = Jsoup.connect(nextURL).cookies(response.cookies())
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.115 Safari/537.36")
                        .timeout(10000)
                        .get();
                //判断此页是否有下一页
                checkNext = targetDoc.select("form");
                checkTemp = checkNext.text();
                rest++;
                if (rest == 7 )  {
                    rest = 0;
                    Thread.sleep(10000);
                }
                }
            inputIntoFile.close();
            }catch (Exception e)  {
                e.printStackTrace();
            }

    }
}
