import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * Created by lewin on 3/8/15.
 */
public class FetchDataIntoDifferentURL {
    public String movieName; //电影名称
    public String moviePicture;  //电影画质
    public String movieClassification;  //电影分类
    public String movieArea;  //电影地区
    public String movieYear; //电影年份
    public String movieDirector;  //电影导演
    public String movieStar;  //电影导演
    public String movieScore;  //电影豆瓣评分
    public String movieSummary; //电影简介
    public String movieJPG;  //电影海报
    public String movieDownload;  //电影下载链接

    public void FetchDataIntoDifferentURL(List list) {
            for (int i = 0; i < list.size(); i++) {
                try {
//            Object object = list.get(i);
//            String movieURL = object.toString();
                //userAgent一定要配置,不然网站可能会认为你是爬虫或者什么不能访问
                Document document = Jsoup.connect(list.get(i).toString())
//                Document document = Jsoup.connect("http://www.tiantangbbs.com/thread-278909-1-1.html")  //用作测试
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
                        .timeout(10000)
                        .get();
                //使用select也可以   select直接定位标签  get。。。定位含有某个属性的标
                Elements elements = document.getElementsByClass("cgtl");//现在用的这个比较方便
                String string = elements.text();
                //将字符串解析
                movieName = string.substring(0,string.indexOf("影片信息")-1);
                moviePicture = string.substring(string.indexOf("画 质: ")+5,string.indexOf("分 类")-1);
                movieClassification = string.substring(string.indexOf("分 类: ")+5,string.indexOf("地 区:")-1);
                movieArea = string.substring(string.indexOf("地 区: ")+5,string.indexOf("年 份: ")-1);
                movieYear = string.substring(string.indexOf("年 份: ")+5,string.indexOf("导演/编剧: ")-1);
                movieDirector = string.substring(string.indexOf("导演/编剧: ")+7 , string.indexOf("主 演: ")-1);
                movieStar = string.substring(string.indexOf("主 演: ")+5 , string.indexOf("豆瓣评分: ")-1);
                movieScore = string.substring(string.indexOf("豆瓣评分: ")+6 , string.indexOf("[")-1);
                ///将字符串打印出来后面写入不同的文件
                System.out.println(movieName);
                System.out.println(moviePicture);
                System.out.println(movieClassification);
                System.out.println(movieArea);
                System.out.println(movieYear);
                System.out.println(movieDirector);
                System.out.println(movieStar);
                System.out.println(movieScore);
//                System.out.println(string);   //测试的字符串
                //有时候定位不好定位时候 可以先抓取大标签  然后删掉没用的标签
                //remove方法删除了整个文档树中的指定标签
                elements = document.select("div[class=t_fsz]");
                //选取第一个符合条件的标签
                Element contentEle = elements.first();
                //删除 ignore_js_op 标签
                Elements temps = contentEle.select("ignore_js_op");
                temps.remove();
                //继续获取想要的标签
                Elements ele = contentEle.getElementsByClass("t_f");
                //对获取的标签进行字符串解析
                string = ele.text();
                if (string.contains("剧情简介")) {
                    string = string.replaceAll("<br />", "");
                    if (string.contains("转自")) {
                        System.out.println(string.substring(string.indexOf("剧情简介") + 6, string.indexOf("转自")));
                    }
                    else {
                        string = string.replaceAll("·"," ");
                        System.out.println(string);
                    }
//                System.exit(1);  //测试只执行一次
                }
                }catch (Exception e)  {
                    e.printStackTrace();
                    continue;
                }
            }
    }
}
