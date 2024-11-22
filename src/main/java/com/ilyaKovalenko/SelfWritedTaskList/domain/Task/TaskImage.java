package com.ilyaKovalenko.SelfWritedTaskList.domain.Task;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TaskImage {

    private MultipartFile file;

}
