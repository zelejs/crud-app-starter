package com.jfeat.dev.logs.service.impl;

import com.jfeat.dev.logs.service.DevLogsService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


@Service
public class DevLogsServiceImpl implements DevLogsService {

    public File[] orderByDate(String filePath) {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File f1, File f2) {
                long diff = f1.lastModified() - f2.lastModified();
                if (diff > 0)
                    return -1;
                else if (diff == 0)
                    return 0;
                else
                    return 1;//如果 if 中修改为 返回-1 同时此处修改为返回 1  排序就会是递减,如果 if 中修改为 返回1 同时此处修改为返回 -1  排序就会是递增,
            }

            public boolean equals(Object obj) {
                return true;
            }

        });
        return files;
    }


}
