package com.zhihu.matisse.internal.utils;

import android.provider.MediaStore;
import android.util.Log;

import com.zhihu.matisse.internal.entity.SelectionSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * IgnoredPathHelper
 * Created by fionera on 2018-11-09 in Matisse.
 */
public class IgnoredPathHelper {

    /**
     * 生成新查询SQL
     *
     * @param selection 原place sql
     * @param selectionArgs 原place args
     * @param placeHolder placeHolder
     * @return {@link SelectionConfig}
     */
    public static SelectionConfig generateIgnoreSql(String selection, String[] selectionArgs,
                                                    String placeHolder) {
        /*
          忽略对应路径的图片，为0时不忽略
         */
        String ignoredPathSql;
        String[] ignorePaths;
        if ((ignorePaths = SelectionSpec.getInstance().getIgnoredPaths()).length == 0) {
            ignoredPathSql = "";
        } else {
            StringBuilder sb = new StringBuilder();
            String startPart = "(";
            String endPart = " 1=1) AND";
            sb.append(startPart);
            for (String ignored : ignorePaths) {
                sb.append(MediaStore.MediaColumns.DATA).append(" not like ? and ");
            }
            sb.append(endPart);
            ignoredPathSql = sb.toString();
        }

        String finalSelection = selection.replace(placeHolder, ignoredPathSql);
        List<String> selectionList = new ArrayList<>(Arrays.asList(ignorePaths));
        selectionList.addAll(Arrays.asList(selectionArgs));
        String[] finalSelectionArgs = new String[selectionList.size()];
        selectionList.toArray(finalSelectionArgs);

        Log.d("AlbumLoader", finalSelection + "|" + finalSelectionArgs[0]);

        return new SelectionConfig(finalSelection, finalSelectionArgs);
    }

    public static class SelectionConfig {
        public String selection;
        public String[] selectionArgs;

        SelectionConfig(String selection, String[] selectionArgs) {
            this.selection = selection;
            this.selectionArgs = selectionArgs;
        }
    }
}
