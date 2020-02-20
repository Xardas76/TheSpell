package com.example.thespell;


class Spell {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    private String name;
    private String discription;
    private boolean perfectCast;

    Spell(){
        perfectCast = false;
    }

    public void setPerfectCast(){
        perfectCast = true;
    }

    public int getMight() {
        return might;
    }

    public void setMight(int might) {
        this.might = might;
    }

    public int getManacost() {
        return manacost;
    }

    public void setManacost(int manacost) {
        this.manacost = manacost;
    }

    public int getMaxpoints() {
        return maxpoints;
    }

    public void setMaxpoints(int maxpoints) {
        this.maxpoints = maxpoints;
    }


    private int maxpoints;
    private int might;
    private int manacost;
    //image
    //sound

    public boolean castisPerfect(){ return perfectCast?true: false; }
}
