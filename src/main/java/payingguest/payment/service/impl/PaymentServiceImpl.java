/***************************************************************************************
 * MIT License
 *
 * Copyright (c) 2022 Mrinmoybits
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

package payingguest.payment.service.impl;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import payingguest.payment.domain.Payment;
import payingguest.payment.repository.PaymentRepository;
import payingguest.payment.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository mPaymentRepository;

    @Override
    public Iterable<Payment> loadAllPayments() {
        return mPaymentRepository.findAll();
    }

    @Transactional
    @Override
    public void createPayment(Payment pPayment) {
        mPaymentRepository.save(pPayment);
    }

    @Override
    public Optional<Payment> findPaymentByPaymentId(Long pPaymentId) {
        return mPaymentRepository.findById(pPaymentId);
    }

    @Override
    public Iterable<Payment> findPaymentByGuestId(long pGuestId) {
        return mPaymentRepository.findByGuestId(pGuestId);
    }

    @Override
    @Transactional
    public void deletePaymentForGuest(long pGuestId) {
        mPaymentRepository.deleteByGuestId(pGuestId);
    }
}
