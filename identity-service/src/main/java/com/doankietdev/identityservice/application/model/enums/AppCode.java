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
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "Not found", HttpStatus.NOT_FOUND),
    AUTH_FAILURE(HttpStatus.UNAUTHORIZED.value(), "Unauthenticated", HttpStatus.UNAUTHORIZED),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "Access denied", HttpStatus.FORBIDDEN),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error, please try again later", HttpStatus.INTERNAL_SERVER_ERROR),
    RATE_LIMIT_ERROR(HttpStatus.TOO_MANY_REQUESTS.value(), "Access too frequently, please try again later", HttpStatus.TOO_MANY_REQUESTS),

    /**
     * User - code: 100xx
     */
    USER_NOT_FOUND(10001, "Account does not exist", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(10002, "Account already exists", HttpStatus.CONFLICT),
    ACCOUNT_VERIFY_IMPOSSIBLE(10003, "Unable to verify account", HttpStatus.BAD_REQUEST),
    ACCOUNT_NOT_VERIFIED(10004, "Your account not verified", HttpStatus.BAD_REQUEST),
    ACCOUNT_INACTIVE(10005, "Your account has been banned or disabled", HttpStatus.BAD_REQUEST),
    CredentialIncorrect(10005, "Incorrect username or password", HttpStatus.BAD_REQUEST),

    /**
     * OTP - code: 101xx
     */
    OTP_INVALID(10101, "Incorrect OTP", HttpStatus.BAD_REQUEST),
    OTP_EXPIRED(10102, "Expired OTP", HttpStatus.BAD_REQUEST),
    // USER_SESSION_EXPIRED(20004, "Phiên đăng nhập của người dùng đã hết hạn, vui lòng đăng nhập lại"),
    // USER_PERMISSION_ERROR(20005, "Quyền hạn không đủ"),
    // USER_PASSWORD_ERROR(20010, "Mật khẩu không chính xác"),
    // USER_AUTH_ERROR(20005, "Quyền hạn không đủ"),
    // EMPLOYEE_DISABLED(20031, "Nhân viên đã bị vô hiệu hóa"),


    /**
     * Cửa hàng
     */

    // STORE_NOT_FOUND(50001, "Không tìm thấy cửa hàng"),
    // STORE_NAME_ALREADY_EXISTS(50002, "Tên cửa hàng đã tồn tại!"),
    // STORE_ALREADY_HAS_STORE(50003, "Bạn đã sở hữu cửa hàng!"),
    // STORE_NOT_OPENED(50004, "Thành viên này chưa mở cửa hàng"),
    // STORE_NOT_LOGGED_IN(50005, "Chưa đăng nhập vào cửa hàng"),
    // STORE_CLOSED(50006, "Cửa hàng đóng cửa, vui lòng liên hệ với quản trị viên"),
    // STORE_DELIVER_PRODUCT_ADDRESS(50007, "Vui lòng điền địa chỉ giao hàng của nhà cung cấp"),
    // FREIGHT_TEMPLATE_NOT_FOUND(50010, "Mẫu hiện tại không tồn tại"),
    // STORE_STATUS_IN_PROGRESS(50011, "Cửa hàng đang trong quá trình đăng ký hoặc phê duyệt, vui lòng không thực hiện thao tác lặp lại"),
    // STORE_SHIPPING_ADDRESS_REQUIRED(50012, "Vui lòng điền địa chỉ giao hàng"),

    /**
     * Ngoại lệ hệ thống
     */
    ;


    int code;
    String message;
    HttpStatusCode statusCode;
}