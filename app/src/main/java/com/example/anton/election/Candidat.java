package com.example.anton.election;

import android.os.Parcel;
import android.os.Parcelable;

public class Candidat implements Parcelable {

    int id,votes;
    double totalVote;
    String firstname,secondname,thirdname,party,description,web,image;

    Candidat(int id, String firstname, String secondname, String thirdname,int votes,double totalVote, String description, String party, String web,String image){

        this.firstname = firstname;
        this.secondname = secondname;
        this.votes = votes;
        this.totalVote = totalVote;
        this.thirdname = thirdname;
        this.description = description;
        this.party = party;
        this.web = web;
        this.image = image;
        this.id = id;


    }

    Candidat(){

        this.firstname = null;
        this.secondname = null;
        this.votes = 0;
        this.totalVote = 0;
        this.thirdname = null;
        this.description = null;
        this.party = null;
        this.web = null;
        this.image = null;
        this.id = 0;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeInt(votes);
        dest.writeDouble(totalVote);
        dest.writeString(firstname);
        dest.writeString(thirdname);
        dest.writeString(secondname);
        dest.writeString(description);
        dest.writeString(party);
        dest.writeString(web);
        dest.writeString(image);


    }

    public Candidat(Parcel in) {
        id = in.readInt();
        votes = in.readInt();
        totalVote = in.readDouble();
        firstname = in.readString();
        thirdname = in.readString();
        secondname = in.readString();
        description = in.readString();
        party = in.readString();
        web = in.readString();
        image = in.readString();

    }

    public static final Parcelable.Creator<Candidat> CREATOR = new Parcelable.Creator<Candidat>() {
        public Candidat createFromParcel(Parcel in) {
            return new Candidat(in);
        }

        public Candidat[] newArray(int size) {
            return new Candidat[size];
        }
    };

}
