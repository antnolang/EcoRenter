package com.ispp.EcoRenter.model;

import lombok.Data;

@Data
public class ChargeRequest {

    public enum Currency {
        EUR, USD;
    }
    private String description;
    private Double amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Double getAmount(){
        return this.amount;
    }

    public void setAmount(Double amount){
        this.amount = amount;
    }

    public Currency getCurrency(){
        return this.currency;
    }

    public void setCurrency(Currency currency){
        this.currency = currency;
    }

    public String getStripeEmail(){
        return this.stripeEmail;
    }

    public void setStripeEmail(String stripeEmail){
        this.stripeEmail = stripeEmail;
    }

    public String getStripeToken(){
        return this.stripeToken;
    }

    public void setStripeToken(String stripeToken){
        this.stripeToken = stripeToken;
    }


}