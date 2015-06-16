import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

/**
 * Created by lewin on 3/8/15.
 * 抓取电影天堂（www.tiantangbbs.com）的最新的电影
 */
public class main {
    public static void main(String[] args) throws IOException {
        //获取天堂BBS的HTML文档
        Document document = Jsoup.connect("http://www.xunleigang.com/")
                .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
                .timeout(10000) //避免网不好出现的超时问题
                .get();
        while (detectNext(document) == true) {
            //获取天堂BBS上面的电影URL
            Elements elements = document.select("a");
            //获取电影海报的链接集合
            Elements elements1 = document.select("img");
            List urlList = new ArrayList();
            List jpgList = new ArrayList();
            for (Element element : elements) {
                //抓取到URL
                String movieURL = element.attr("abs:href");
                //提取到有用的的URL
                if (movieURL.contains("thread") && movieURL.contains("html") && !movieURL.contains("83494")) {
                    //将抓取到的URL添加到List中
                    urlList.add(movieURL);
                }
            }

            for (Element element : elements1) {
                String movieJPG = element.attr("abs:src");
                if (movieJPG.contains(".jpg"))  {
                    jpgList.add(movieJPG);
                }
            }
            //将List中重复的URL去除
            urlList = removeDuplicate(urlList);
//      //  计数URL个数测试去重是否正确
//        int i = 0;
//        for ( i = 0 ; i < urlList.size() ; i++ )  {
//            System.out.println(urlList.get(i));
//        }
//        System.out.println(i);

            //进入list中的每一个URL进行抓取
            FetchDataIntoDifferentURL fetchDataIntoDifferentURL = new FetchDataIntoDifferentURL();
            fetchDataIntoDifferentURL.FetchDataIntoDifferentURL(urlList , jpgList);
            //获取下一页的网址
            Elements tem = document.getElementsByClass("nxt");
            String movieURLs = tem.attr("abs:href");
            document = Jsoup.connect(movieURLs)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
                    .timeout(10000) //避免网不好出现的超时问题
                    .get();
        }
    }

    //将List中重复的URL去除
    public static List removeDuplicate(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            Object object = iterator.next();
            if (set.add(object)) {
                newList.add(object);
            }
        }
        return newList;
    }
    //判断有没有下一页
    public static boolean detectNext(Document doc ){
        Elements elements = doc.getElementsByClass("nxt");
        String tem = elements.text();
        if (tem.contains("下一页"))  {
            return true;
        }
        else {
            System.out.println("连接不到服务器！");
            return false;
        }
    }
}
