package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.business.api.domain.*;
import cn.wolfcode.wolf2w.business.query.SearchQuery;
import cn.wolfcode.wolf2w.business.service.ISearchService;
import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.member.api.domain.UserInfo;
import io.prometheus.client.Info;
import io.prometheus.client.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private ISearchService searchService;

    
    @GetMapping("/search")
    public R<?> search(SearchQuery qo) throws InvocationTargetException, IllegalAccessException {

        System.err.println(qo.getKeyword());

        switch (qo.getType()) {
            case 1:
                return R.ok(searchStrategy(qo));
            case 2:
                return R.ok(searchNote(qo));
            case 3:
                return R.ok(searchUser(qo));
            case 4:
                return R.ok(searchQuestion(qo));
            default:
                return R.ok(searchAll(qo));
        }

    }



    private Object searchAll(SearchQuery qo) throws InvocationTargetException, IllegalAccessException {
        Page<UserInfo> userPage = searchService.searchHighLight(UserInfo.class, UserInfoEs.class, qo, "nickname", "info", "city");
        Page<Strategy> strategyPage = searchService.searchHighLight(Strategy.class, StrategyEs.class, qo, "title", "subTitle", "summary");
        Page<Note> notePage = searchService.searchHighLight(Note.class, NoteEs.class, qo, "title", "summary");
        Page<Destination> destinationPage = searchService.searchHighLight(Destination.class, DestinationEs.class, qo, "name", "info");
        long total = userPage.getTotalElements()+
                strategyPage.getTotalElements()+
                notePage.getTotalElements()+
                destinationPage.getTotalElements();
        List<UserInfo> userList = userPage.getContent();
        List<Strategy> strategyList = strategyPage.getContent();
        List<Note> noteList = notePage.getContent();
        List<Destination> destinationList = destinationPage.getContent();
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("users", userList);
        map.put("strategies", strategyList);
        map.put("notes", noteList);
        map.put("dests", destinationList);
        return map;
    }


    private Object searchQuestion(SearchQuery qo) throws InvocationTargetException, IllegalAccessException {
        Page<QaQuestions> qaQuestions = searchService.searchHighLight(QaQuestions.class, QuestionEs.class, qo, "title", "detailDoubt");
        return qaQuestions;
    }
    private Object searchUser(SearchQuery qo) throws InvocationTargetException, IllegalAccessException {
        Page<UserInfo> userInfos = searchService.searchHighLight(UserInfo.class, UserInfoEs.class, qo, "city", "nickname");
        return userInfos;
    }

    private Object searchNote(SearchQuery qo) throws InvocationTargetException, IllegalAccessException {
        Page<Note> notes = searchService.searchHighLight(Note.class, NoteEs.class, qo, "title", "Summary");
        return notes;
    }

    private Object searchStrategy(SearchQuery qo) throws InvocationTargetException, IllegalAccessException {
        Page<Strategy> strategies = searchService.searchHighLight(Strategy.class, StrategyEs.class, qo, "title", "subTitle", "summary");
        return strategies;
    }
}
