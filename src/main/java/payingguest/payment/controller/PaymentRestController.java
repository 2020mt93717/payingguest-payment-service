/***************************************************************************************
 * MIT License
 *
 * Copyright (c) 2022 2020mt93717, Mrinmoybits
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * **************************************************************************************/
package payingguest.payment.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import payingguest.payment.domain.Guest;
import payingguest.payment.domain.Payment;
import payingguest.payment.service.PaymentService;

@RestController
public class PaymentRestController {

    @Autowired
    private PaymentService mPaymentService;

    @Autowired
    private RestTemplate mRestTemplate;

    @GetMapping("/payment/guest/{GuestId}")
    public Iterable<Payment> findPaymentsByGuestId(@PathVariable("GuestId") Long pGuestId) {
        return mPaymentService.findPaymentByGuestId(pGuestId);
    }

    @GetMapping("/payment")
    public Iterable<Payment> loadAllPayments() {
        return mPaymentService.loadAllPayments();
    }

    @GetMapping("/payment/{PaymentId}")
    public Payment findPaymentsByPaymentId(@PathVariable("PaymentId") Long pPaymentId) {
        Optional<Payment> myPayment = mPaymentService.findPaymentByPaymentId(pPaymentId);
        return myPayment.isPresent() ? myPayment.get() : null;
    }

    @PostMapping(
            value = "/payment/create",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<Payment> createGuest(@RequestBody Payment pPayment) {
        if (pPayment == null || pPayment.getGuestId() == 0) {
            return new ResponseEntity<>(pPayment, HttpStatus.BAD_REQUEST);
        }
        // Validate if Guest Exist
        // We shouldnt generate payment entry for non existant guests
        // Use service discovery to get the response
        String mGuestUrl = "http://guest-service/guest/id/" + pPayment.getGuestId();
        Guest myGuestResponse = mRestTemplate.getForObject(mGuestUrl, Guest.class);
        if (myGuestResponse == null || myGuestResponse.getGuestId() != pPayment.getGuestId()) {
            return new ResponseEntity<>(pPayment, HttpStatus.BAD_REQUEST);
        }
        mPaymentService.createPayment(pPayment);
        return new ResponseEntity<>(pPayment, HttpStatus.OK);

    }
}
