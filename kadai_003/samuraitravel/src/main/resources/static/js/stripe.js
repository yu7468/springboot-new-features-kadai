const stripe = Stripe('pk_test_51PZUJXRxbCwL8rrlcU20lyEF30Scgg8aSDBk5GPRzuWEgwxBor9btkjbGiooWrBck4Ct1alxTjYYQYunvPhvazdK00junYLkkJ');
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });