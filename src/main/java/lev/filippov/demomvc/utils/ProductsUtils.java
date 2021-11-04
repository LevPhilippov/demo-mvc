package lev.filippov.demomvc.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static lev.filippov.demomvc.services.ProductService.filtersSet;

public class ProductsUtils {

    public static Map<String, String> parseFilters(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (String s : filtersSet) {
            if (request.getParameter(s) != null && !request.getParameter(s).isEmpty())
                params.put(s, request.getParameter(s));
        }
        return params;
    }

    public static String parseFiltersMapToString(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) -> {
            for (String s : filtersSet) {
                if (key.equals(s))
                    sb.append(key + "=" + value + "&");
            }
        });
        return sb.toString();
    }
}
