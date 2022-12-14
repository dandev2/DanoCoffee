package com.example.danocoffee.data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="orderlist")
public class OrderList implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) //int.autoincrement
    private int orlId;

    @ManyToOne
    @JoinColumn(name="mId")
    private Menu mId; //메누아이디

    @ManyToOne
    @JoinColumn(name="pId")
    private Pay pId; //결제아이디

    @Column
    private int orlPayment; //가격
    private int orlCount; //수량

    public OrderList(Menu mId, Pay getpId, int getorlCount, int orlPayment) {
        this.mId = mId;
        this.pId = getpId;
        this.orlCount = getorlCount;
        this.orlPayment = orlPayment;
    }

    public int getOrlId() {
        return orlId;
    }
    public void setOrlId(int orlId) {
        this.orlId = orlId;
    }

    public Menu getmId() {
        return mId;
    }

    public void setmId(Menu mId) {
        this.mId = mId;
    }

    public Pay getpId() {
        return pId;
    }

    public void setpId(Pay pId) {
        this.pId = pId;
    }

    public int getOrlPayment() {
        return orlPayment;
    }

    public void setOrlPayment(int orlPayment) {
        this.orlPayment = orlPayment;
    }

    public int orlCount() {
        return orlCount;
    }

    public void orlCount(int orlCount) {
        this.orlCount = orlCount;
    }


    public OrderList() {
    }
}