package com.doankietdev.identityservice.application.model.enums;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * Trả về mã trạng thái
 * Chữ số đầu tiên: 1: Sản phẩm; 2: Người dùng; 3: Giao dịch,
 * 4: Khuyến mãi, 5: Cửa hàng, 6: Trang web, 7: Cài đặt, 8: Khác
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum AppCode {
    SUCCESS(HttpStatus.OK.value(), "Success", HttpStatus.OK),
    PARAMS_ERROR(HttpStatus.BAD_REQUEST.value(), "Invalid parameters", HttpStatus.BAD_REQUEST),
    ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "This endpoint is not supported", HttpStatus.NOT_FOUND),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error, please try again later",
            HttpStatus.INTERNAL_SERVER_ERROR),
    RATE_LIMIT_ERROR(HttpStatus.TOO_MANY_REQUESTS.value(), "Access too frequently, please try again later",
            HttpStatus.TOO_MANY_REQUESTS),

    /**
     * User - code: 100xx
     */
    USER_NOT_FOUND(10001, "User not found", HttpStatus.NOT_FOUND),
    ACCOUNT_NOTFOUND(10002, "Account does not exist", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(10003, "Account already exists", HttpStatus.CONFLICT),

    /**
     * OTP - code: 101xx
     */
    OTP_INVALID(10101, "Incorrect OTP", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(10102, "Expired OTP", HttpStatus.BAD_REQUEST),

    /**
     * Auth - code: 102xx
     */
    ACCOUNT_VERIFY_IMPOSSIBLE(10201, "Unable to verify account", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_VERIFIED(10202, "Your account not verified", HttpStatus.BAD_REQUEST),
    ACCOUNT_INACTIVE(10203, "Your account has been banned or disabled", HttpStatus.BAD_REQUEST),
    CredentialIncorrect(10204, "Incorrect username or password", HttpStatus.BAD_REQUEST),
    TOKEN_MISSING(10205, "Missing token", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(10206, "Invalid token", HttpStatus.UNAUTHORIZED),
    ACCESS_TOKEN_EXPIRED(10207, "Session expired. Please log back in.", HttpStatus.UNAUTHORIZED),
    REFESH_TOKEN_EXPIRED(10208, "Session expired. Please log back in.", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(10209, "Access denied", HttpStatus.FORBIDDEN),

    /**
     * Cửa hàng
     */

    // STORE_NOT_FOUND(50001, "Không tìm thấy cửa hàng"),
    // STORE_NAME_ALREADY_EXISTS(50002, "Tên cửa hàng đã tồn tại!"),
    // STORE_ALREADY_HAS_STORE(50003, "Bạn đã sở hữu cửa hàng!"),
    // STORE_NOT_OPENED(50004, "Thành viên này chưa mở cửa hàng"),
    // STORE_NOT_LOGGED_IN(50005, "Chưa đăng nhập vào cửa hàng"),
    // STORE_CLOSED(50006, "Cửa hàng đóng cửa, vui lòng liên hệ với quản trị viên"),
    // STORE_DELIVER_PRODUCT_ADDRESS(50007, "Vui lòng điền địa chỉ giao hàng của nhà
    // cung cấp"),
    // FREIGHT_TEMPLATE_NOT_FOUND(50010, "Mẫu hiện tại không tồn tại"),
    // STORE_STATUS_IN_PROGRESS(50011, "Cửa hàng đang trong quá trình đăng ký hoặc
    // phê duyệt, vui lòng không thực hiện thao tác lặp lại"),
    // STORE_SHIPPING_ADDRESS_REQUIRED(50012, "Vui lòng điền địa chỉ giao hàng"),

    /**
     * Ngoại lệ hệ thống
     */
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}