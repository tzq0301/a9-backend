package a9.controller;

import a9.entity.article.Article;
import a9.entity.article.ArticleTags;
import a9.entity.baidu.Content;
import a9.entity.baidu.Event;
import a9.entity.result.Result;
import a9.entity.template.Template;
import a9.entity.user.User;
import a9.service.ArticleService;
import a9.service.BaiduService;
import a9.service.TemplateService;
import a9.service.UserService;
import a9.util.JWTUtils;
import com.google.common.base.Strings;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static a9.entity.result.ResultEnum.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author tzq0301
 * @version 1.0
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    private final ArticleService articleService;

    private final UserService userService;

    private final TemplateService templateService;

    private final JWTUtils jwtUtils;

    private final BaiduService baiduService;

    public ArticleController(ArticleService articleService,
                             UserService userService,
                             TemplateService templateService,
                             JWTUtils jwtUtils,
                             BaiduService baiduService) {
        this.articleService = articleService;
        this.userService = userService;
        this.templateService = templateService;
        this.jwtUtils = jwtUtils;
        this.baiduService = baiduService;
    }

    @PostMapping("/save")
    public Result<String> save(@RequestBody Article article, HttpServletRequest request) {
        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        if (Objects.isNull(article.getAuthorId())) {
            article.setAuthorId(jwtUtils.getSubject(request.getHeader(AUTHORIZATION)));
        }

        Optional<User> user = userService.findUserById(authorId);
        if (user.isEmpty()) {
            return Result.error(NO_SUCH_USER);
        }

        return Result.success(articleService.save(user.get(), article).toString());
    }

    @PostMapping("/save/template_id/{template_id}")
    public Result<String> save(@PathVariable("template_id") String templateId, HttpServletRequest request) {
        Optional<Template> template = templateService.findById(new ObjectId(templateId));
        if (template.isEmpty()) {
            return Result.error(NO_SUCH_TEMPLATE);
        }

        Article article = new Article();
        article.setContent(template.get().getContent());
        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        article.setAuthorId(authorId);
        return Result.success(articleService.save(
                userService.findUserById(authorId).orElse(null), article).toString());
    }

    @PostMapping("/save/template_id/{template_id}/title/{title}")
    public Result<String> save(@PathVariable("template_id") String templateId,
                               @PathVariable("title") String title,
                               HttpServletRequest request) {
        Optional<Template> template = templateService.findById(new ObjectId(templateId));
        if (template.isEmpty()) {
            return Result.error(NO_SUCH_TEMPLATE);
        }

        Article article = new Article();
        article.setTitle(title);
        article.setContent(template.get().getContent());
        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        article.setAuthorId(authorId);
        return Result.success(articleService.save(
                userService.findUserById(authorId).orElse(null), article).toString());
    }

    @PatchMapping("/article_id/{article_id}/tag")
    @Deprecated
    public Result<?> addTag(@PathVariable("article_id") String articleId,
                            @RequestBody String tag) {
        Optional<Article> article = findArticleById(articleId);

        if (article.isEmpty()) {
            return Result.error(NO_SUCH_ARTICLE);
        }

        var result = articleService.addTag(article.get(), tag);

        if (result.isEmpty()) {
            return Result.error(TAG_DUPLICATE);
        }

        return Result.success(result.get().toString());
    }

    @PutMapping("/article_id/{article_id}/tags")
    public Result<?> updateTags(@PathVariable("article_id") String articleId,
                                @RequestBody ArticleTags articleTags) {
        Optional<Article> article = findArticleById(articleId);

        if (article.isEmpty()) {
            return Result.error(NO_SUCH_ARTICLE);
        }

        articleService.updateTags(article.get(), articleTags.getTags());

        return Result.success();
    }

    @PatchMapping("/")
    public Result<?> update(@RequestBody Article article, HttpServletRequest request) {
        Optional<Article> articleInDB = findArticleById(article.getId().toString());

        if (articleInDB.isEmpty()) {
            return Result.error(NO_SUCH_ARTICLE);
        }

        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        if (!Objects.equals(authorId, articleInDB.get().getAuthorId())) {
            return Result.error(NO_PERMISSION);
        }

        if (Strings.isNullOrEmpty(article.getTitle())) {
            article.setTitle(articleInDB.get().getTitle());
        }

        if (Strings.isNullOrEmpty(article.getAbstraction())) {
            article.setAbstraction(articleInDB.get().getAbstraction());
        }

        if (Strings.isNullOrEmpty(article.getContent())) {
            article.setContent(articleInDB.get().getContent());
        }

        if (article.getAuthorId() == null) {
            article.setAuthorId(articleInDB.get().getAuthorId());
        }

        if (article.getTags() == null) {
            article.setTags(articleInDB.get().getTags());
        }

        return Result.success(articleService.update(article));
    }

    @DeleteMapping("/article_id/{article_id}")
    public Result<?> delete(@PathVariable("article_id") String articleId, HttpServletRequest request) {
        Optional<Article> article = findArticleById(articleId);

        if (article.isEmpty()) {
            return Result.error(NO_SUCH_ARTICLE);
        }

        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));
        if (!Objects.equals(authorId, article.get().getAuthorId())) {
            return Result.error(NO_PERMISSION);
        }

        Optional<User> author = userService.findUserById(authorId);
        if (author.isEmpty()) {
            return Result.error(NO_SUCH_USER);
        }

        var result = articleService.delete(author.get(), article.get());

        if (result.isEmpty()) {
            return Result.error(TAG_DUPLICATE);
        }

        return Result.success(result.get().toString());
    }

    @GetMapping("/article_id/{article_id}")
    public Result<?> find(@PathVariable("article_id") String articleId, HttpServletRequest request) {
        Optional<Article> article = findArticleById(articleId);

        if (article.isEmpty()) {
            return Result.error(NO_SUCH_ARTICLE);
        }

        if (!Objects.equals(jwtUtils.getSubject(request.getHeader(AUTHORIZATION)), article.get().getAuthorId())) {
            return Result.error(NO_PERMISSION);
        }

        return Result.success(article.get());
    }

    @GetMapping("/articles")
    public Result<?> listArticles(HttpServletRequest request) {
        ObjectId authorId = jwtUtils.getSubject(request.getHeader(AUTHORIZATION));

        Optional<User> user = userService.findUserById(authorId);
        if (user.isEmpty()) {
            return Result.error(NO_SUCH_USER);
        }

        return Result.success(articleService.listArticlesByAuthorId(user.get().getId())
                .stream().map(Article::cleanAndSubContent).collect(Collectors.toList()));
    }

    @PostMapping("/correction")
    public Result<String> checkTextCorrection(@RequestBody String text) throws TencentCloudSDKException {
        return Result.success(articleService.checkTextCorrection(text));
    }

    @GetMapping("/events")
    public Result<List<Event>> listEvents() {
        return Result.success(baiduService.listEvents());
    }

    @GetMapping("/event_id/{event_id}")
    public Result<Content> findEventById(@PathVariable("event_id") String eventId) {
        return Result.success(baiduService.findContentByEventId(eventId));
    }

    private Optional<Article> findArticleById(final String articleId) {
        Optional<Article> article;

        try {
            article = articleService.findArticleById(articleId);
        } catch (IllegalArgumentException exception) {
            return Optional.empty();
        }

        if (article.isEmpty()) {
            return Optional.empty();
        }

        return article;
    }

}
