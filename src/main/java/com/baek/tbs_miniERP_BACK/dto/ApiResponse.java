package com.baek.tbs_miniERP_BACK.dto;


import com.baek.tbs_miniERP_BACK.util.ErrorCode;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

public class ApiResponse<T> {
    private final boolean success;
    private final String code;
    private final String message;
    private final T data;

    private ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 성공 응답
    /*
    {
        "success": true,
        "code": null,
        "message": null,
        "data": {
            "userId": 1,
            "email": "slgi@example.com"
        }
    }
    */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "200", "요청성공", data);
    }

    // 실패 응답 (ErrorCode 기반)
    /*
    {
        "success": false,
        "code": "C999",
        "message": "커스텀 오류 발생",
        "data": null
    }
    */
    public static ApiResponse<?> fail(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }

    // (선택) 실패 응답 - 커스텀 메시지 직접 전달
    public static ApiResponse<?> fail(String code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

}
