package com.baek.tbs_miniERP_BACK.util;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SearchTokens {
    private final List<String> mustInclude = new ArrayList<>();
    private final List<String> mustExclude = new ArrayList<>();
    private final List<String> include = new ArrayList<>();

    public static SearchTokens parse(String raw) {
        SearchTokens t = new SearchTokens();
        if(raw == null) return t;

        String str = raw.trim().toLowerCase();
        if(str.isEmpty()) return t;

        for(String s : str.split("\\s+")) {
            if(s.startsWith("+") && s.length() > 1) {
                t.mustInclude.add(s.substring(1));
            } else if (s.startsWith("-") && s.length() > 1) {
                t.mustExclude.add(s.substring(1));
            } else {
                t.include.add(s);
            }
        }
        return t;
    }
}
