package mate.academy.bookingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.academy.bookingapp.dto.payment.CreatePaymentRequestDto;
import mate.academy.bookingapp.dto.payment.PaymentResponseDto;
import mate.academy.bookingapp.model.Payment;
import mate.academy.bookingapp.model.PaymentStatus;
import mate.academy.bookingapp.model.User;
import mate.academy.bookingapp.service.payment.PaymentProvider;
import mate.academy.bookingapp.service.payment.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payments management",
        description = "Endpoints for payments management")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Get all user's Payments",
            description = "Returns pageable dto of all user's Payments")
    public Page<PaymentResponseDto> getAllUsersPayments(Pageable pageable,
                                                        Authentication authentication) {
        Long userId = getCurrentUserId(authentication);
        return paymentService.getAllPayments(userId, pageable);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Get all user's Payments by userId",
            description = "Returns pageable dto of all user's Payments by given userId")
    public Page<PaymentResponseDto> getUserPayments(@RequestParam("user_id") Long userId,
                                                    Pageable pageable) {
        return paymentService.getPaymentsForUser(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Create payment session",
            description = "Creates chosen payment session")
    public Map<String, String> createPaymentSession(
            @Valid @RequestBody CreatePaymentRequestDto request,
            @RequestParam PaymentProvider provider) {
        String sessionUrl = paymentService.createPayment(request.getBookingId(),
                request.getUserId(), provider);
        return Map.of("sessionUrl", sessionUrl);
    }

    @GetMapping("/success")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Handle success",
            description = "Handles success payment")
    public String handleSuccess(@RequestParam("session_id") String sessionId) {
        paymentService.handlePaymentSuccess(sessionId);
        return "Payment successful.";
    }

    @GetMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Handle cancel",
            description = "Handles unsuccess payment")
    public String handleCancel(@RequestParam("session_id") String sessionId) {
        paymentService.handlePaymentCancel(sessionId);
        return "Payment was cancelled. Payment could be done in 24 hours.";
    }

    @PostMapping("/{paymentId}/renew")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @Operation(summary = "Renew payment",
            description = "Renews payment with a given paymentId")
    public Map<String, String> renewPayment(@PathVariable Long paymentId) {
        Payment payment = paymentService.findById(paymentId);

        if (payment.getStatus() != PaymentStatus.EXPIRED) {
            throw new IllegalStateException("Only expired payments can be renewed.");
        }
        String newSessionUrl = paymentService.createPayment(
                payment.getBooking().getId(),
                payment.getBooking().getUser().getId(),
                PaymentProvider.STRIPE
        );
        return Map.of("sessionUrl", newSessionUrl);
    }

    private Long getCurrentUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }
}
