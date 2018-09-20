package com.zsdang.view;

import java.util.List;

public class DataValidateUtils {

    public static boolean validateList(List<?> list) {
        if (list != null) {
            return true;
        }
        return false;
    }
}
