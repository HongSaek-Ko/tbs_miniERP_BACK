package com.baek.tbs_miniERP_BACK.util;

import com.baek.tbs_miniERP_BACK.dto.EmpFilterParams;
import com.baek.tbs_miniERP_BACK.entity.Employee;
import com.baek.tbs_miniERP_BACK.entity.Team;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmpSpecifications {
    private static boolean hasText(String s) {
        return s != null && !s.trim().isEmpty();
    }

    private static String like(String s) {
        return "%" + s.trim().toLowerCase() + "%";
    }

    public static Specification<Employee> byFilters(EmpFilterParams p) {
        return (root, query, cb) -> {
            // export에서 중복 row 방지
            query.distinct(true);

            // 조인 (left join)
            Join<Employee, Team> t = root.join("team", JoinType.LEFT);
            List<Predicate> and = new ArrayList<>();

            if (hasText(p.getEmpName())) {
                and.add(cb.like(cb.lower(t.get("empName")), like(p.getEmpName())));
            }

            if (hasText(p.getEmpPos())) {
                // and.add(cb.equal(e.get("empPos"), p.getEmpPos().trim()));
                // 포함검색 원하면 like로 변경 가능
                and.add(cb.like(cb.lower(t.get("empPos")), like(p.getEmpPos())));
            }

            if (hasText(p.getTeamName())) {
                and.add(cb.like(cb.lower(t.get("teamName")), like(p.getTeamName())));
            }

            if (hasText(p.getEmpStatus())) {
                and.add(cb.like(cb.lower(t.get("empStatus")), like(p.getEmpStatus())));
            }

            // 2) globalSearch 토큰 규칙
            //    - "+토큰": mustInclude (AND)
            //    - "-토큰": mustExclude (NOT)
            //    - 일반토큰: include (AND)
            if (hasText(p.getGlobalSearch())) {
                String raw = p.getGlobalSearch().trim().toLowerCase();
                String[] tokens = raw.split("\\s+");

                List<String> mustInclude = new ArrayList<>();
                List<String> mustExclude = new ArrayList<>();
                List<String> include = new ArrayList<>();

                for (String tok : tokens) {
                    if (tok.startsWith("+") && tok.length() > 1) mustInclude.add(tok.substring(1));
                    else if (tok.startsWith("-") && tok.length() > 1) mustExclude.add(tok.substring(1));
                    else include.add(tok);
                }

                // 프론트 fields와 동일하게 맞춤
                List<Expression<String>> fields = List.of(
                        cb.lower(t.get("empName")),
                        cb.lower(t.get("empPos")),
                        cb.lower(t.get("teamName")),
                        cb.lower(t.get("empStatus"))
                );

                // helper: 한 토큰이 "어느 필드든 포함"이면 true (OR)
                java.util.function.Function<String, Predicate> tokenAnyFieldLike = (token) -> {
                    String pattern = "%" + token + "%";
                    List<Predicate> ors = new ArrayList<>();
                    for (Expression<String> f : fields) {
                        ors.add(cb.like(f, pattern));
                    }
                    return cb.or(ors.toArray(new Predicate[0]));
                };

                // -제외: 하나라도 포함되면 탈락 => NOT(OR)
                for (String token : mustExclude) {
                    and.add(cb.not(tokenAnyFieldLike.apply(token)));
                }

                // +필수: 전부 포함되어야 => AND(OR)
                for (String token : mustInclude) {
                    and.add(tokenAnyFieldLike.apply(token));
                }

                // 일반토큰: 프론트에서 AND로 처리했으니 동일하게 AND(OR)
                for (String token : include) {
                    and.add(tokenAnyFieldLike.apply(token));
                }
            }

            return cb.and(and.toArray(new Predicate[0]));
        };
    }
}
