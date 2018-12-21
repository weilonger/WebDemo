package com.weilong.webdemo.controller;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.weilong.webdemo.entities.Goods;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/jd")
@ResponseBody
@Log4j2
public class JdSpiderController {

    private static String JINGDONG_BASE_URL = "https://search.jd.com/Search";
    private static String JINGDONG_COMMENT_BASE_URL = "https://club.jd.com/comment/productCommentSummaries.action";
    private static String JINGDONG_PRICE_BASE_URL = "https://p.3.cn/prices/get?skuid=J_";
    private static String PREFIX = "https:";

    @RequestMapping("/getbook")
    public List<Goods> getBookByIBSN(@RequestParam String isbn) throws Exception {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setCssEnabled(false);
        //当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        String url = JINGDONG_BASE_URL.concat("?keyword=").concat(isbn).concat("&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=3.his.0.0&page=3&s=1&click=0");
        HtmlPage page = webClient.getPage(url);

        DomNode ul = page.querySelector(".gl-warp");
        List<HtmlListItem> glist = page.getByXPath("//div[@id='J_goodsList'][1]/ul/li");
//        DomNodeList<DomNode> glist = ul.querySelectorAll(".gl-item");
        List<Goods> goods = new ArrayList<>();
        for (HtmlListItem g : glist){
            Goods good = new Goods();
            HtmlDivision d = g.querySelector(".gl-i-wrap");
            String id = g.getAttribute("data-sku");
            HtmlDivision div1 = g.querySelector(".p-img");
            HtmlAnchor a1 = div1.querySelector("a");
            HtmlImage image = a1.querySelector("img");
            String img = PREFIX.concat(image.getAttribute("source-data-lazy-img"));
            String baseurl = a1.getAttribute("href");
            if (!StringUtils.isEmpty(baseurl) && !baseurl.contains("https:")) {
                baseurl = PREFIX.concat(baseurl);
            }
            HtmlDivision div2 = g.querySelector(".p-price");
            String price = div2.querySelector("strong").getTextContent();
            HtmlDivision div3 = g.querySelector(".p-name");
            HtmlAnchor a3 = div3.querySelector("a");
            String baseurl2 = a3.getAttribute("href");
            String name = div3.querySelector("em").getTextContent();
            HtmlDivision div4 = g.querySelector(".p-scroll");
            List<String> imgs = new ArrayList<>();
            if (div4 != null){
                DomNodeList<DomNode> li = div4.querySelectorAll("li");
                for (DomNode l:li) {
                    HtmlAnchor anchor = l.querySelector("a");
                    HtmlImage image2 = anchor.querySelector("img");
                    String img1 = image2.getAttribute("data-lazy-img");
                    if (!StringUtils.isEmpty(img1)) {
                        img1 = PREFIX.concat(image2.getAttribute("data-lazy-img"));
                        imgs.add(img1);
                    }
                }
            }
            String bookstore = null;
            HtmlDivision div5 = g.querySelector(".p-shop") != null? g.querySelector(".p-shop") : g.querySelector(".p-shopnum");
            HtmlAnchor a2 = div5.querySelector("a");
            if (a2 != null) {
                bookstore = a2.getAttribute("title");
            }
            good.setId(id);
            good.setImg(img);
            good.setImgs(imgs);
            good.setUrl(baseurl);
            good.setPrice(price);
            good.setName(name);;
            good.setBookStore(div5.asText());
            goods.add(good);
        }
        log.info("共爬取到" + goods.size() + "条数据");
        return goods;
    }

    @RequestMapping("/getlist")
    public void getlist(@RequestParam String name, @RequestParam String no) throws IOException {
        WebDriver webClient = new HtmlUnitDriver(true);
        String url = JINGDONG_BASE_URL.concat("?keyword=").concat(name).concat("&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&suggest=3.his.0.0&s=1&click=0")
                .concat("&page=").concat(no);
        webClient.get(url);
        List<WebElement> elements = webClient.findElements(By.className("p-name"));
        webClient.close();
//        return null;
    }

}
