package utillities.entidades;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ListaParcelasRelevamiento extends ArrayList<ParcelasRelevamientoSQLiteEntity> implements Parcelable{

    private int mData;
    public ListaParcelasRelevamiento() {
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        int size = this.size();
        dest.writeInt(size);

        for (int i = 0; i < size; i++){
            ParcelasRelevamientoSQLiteEntity parcela = this.get(i);

            //Hago la escritura de los datos
            dest.writeInt(parcela.getId());
            dest.writeInt(parcela.getIdparcela());
            dest.writeInt(parcela.getIdrodales());
            dest.writeString(parcela.getCod_sap());
            dest.writeString(parcela.getComentarios());
            dest.writeString(parcela.getGeometry());
            dest.writeDouble(parcela.getSuperficie());
            dest.writeDouble(parcela.getPendiente());
            dest.writeDouble(parcela.getLat());
            dest.writeDouble(parcela.getLongi());
        }

    }


    public static final Parcelable.Creator<ListaParcelasRelevamiento> CREATOR
            = new Parcelable.Creator<ListaParcelasRelevamiento>() {
        public ListaParcelasRelevamiento createFromParcel(Parcel in) {
            return new ListaParcelasRelevamiento(in);
        }

        public ListaParcelasRelevamiento[] newArray(int size) {
            return new ListaParcelasRelevamiento[size];
        }
    };

    private ListaParcelasRelevamiento(Parcel in) {
        mData = in.readInt();
        readfromParcel(in);
    }

    private void readfromParcel(Parcel in){

        this.clear();
        int size = in.readInt();

        for (int i = 0; i < size; i++){

            ParcelasRelevamientoSQLiteEntity parcela = new ParcelasRelevamientoSQLiteEntity();

            parcela.setId(in.readInt());
            parcela.setIdparcela(in.readInt());
            parcela.setIdrodales(in.readInt());
            parcela.setCod_sap(in.readString());
            parcela.setComentarios(in.readString());
            parcela.setGeometry(in.readString());
            parcela.setSuperficie(in.readDouble());
            parcela.setPendiente(in.readDouble());
            parcela.setLat(in.readDouble());
            parcela.setLongi(in.readDouble());

            this.add(parcela);
        }

    }


    public void addFromLista(ArrayList<ParcelasRelevamientoSQLiteEntity> listaEntities){


        for (ParcelasRelevamientoSQLiteEntity parc : listaEntities){

            this.add(parc);
        }

    }
}
