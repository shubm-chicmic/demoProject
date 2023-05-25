package com.example.demoProject.Controller.MevronController;

import com.example.demoProject.Dto.CreatePayment;
import com.example.demoProject.Dto.CreatePaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Controller
public class PaymentController {


    static long calculateOrderAmount(int[] items) {
        // Replace this constant with a calculation of the order's amount
        // Calculate the order total on the server to prevent
        // people from directly manipulating the amount on the client
        Integer sum = 0;
        for (int i : items) {
            sum += items[i]*100;
        }
        return sum;
    }
    @Value("${stripe.public.key}")
    String stripePublicKey;
    @RequestMapping("/payment")
    public String payment(Model model, @RequestParam(value = "price", required = false, defaultValue = "0") String price) {
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("price", price);
        return "payment-checkout";
    }

    @PostMapping("/create-payment-intent")
    @ResponseBody
    public CreatePaymentResponse createPaymentIntent(@RequestBody CreatePayment createPayment) throws StripeException {
           Object[] objectArray = createPayment.getItems();
            String str2 = "0";
        for(int i = 0; i < objectArray.length; i++){
            String str = objectArray[i].toString();
            int start = -1;
            for (int j = 0; j < str.length(); j++) {
                System.out.println("str = " + str.charAt(j));
                if(str.charAt(j) == '='){
                    start = j + 1;
                    break;
                }
            }
           str2 = str.substring(start, str.length() - 1);
        }

        PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(Long.parseLong(str2))
                            .setCurrency("INR")
                            .setAutomaticPaymentMethods(
                                    PaymentIntentCreateParams.AutomaticPaymentMethods
                                            .builder()
                                            .setEnabled(true)
                                            .build()
                            )
                            .build();

            // Create a PaymentIntent with the order amount and currency
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            CreatePaymentResponse paymentResponse = new CreatePaymentResponse(paymentIntent.getClientSecret());
            return paymentResponse;

    }
}
