package com.aueb.telematics;

import android.app.TimePickerDialog;
import android.os.AsyncTask;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {

    private Button button;

    private GoogleMap mMap;
    MarkerOptions markerOptions = new MarkerOptions();

    private String description;
    private String lineId;
    private double latitude;
    private double longitude;
    private String lineCode;
    private String routeCode;
    private String vehicleId;
    private String timestampOfBusPosition;
    private int routeType;

    LatLng point;

    private String requiredRouteCode;
    private String requiredBusId;

    String requiredHour;
    String requiredMin;
    String requiredDay;

    public  LocalTime requiredTime;
    private  LocalTime busTime;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        button = findViewById(R.id.button);


        final EditText getTime= findViewById(R.id.getTime);

        getTime.setInputType(InputType.TYPE_NULL);
        getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                 int hour = cldr.get(Calendar.HOUR_OF_DAY);
                 int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                TimePickerDialog picker = new TimePickerDialog(MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                requiredHour=String.valueOf(sHour);
                                requiredMin=String.valueOf(sMinute);
                                getTime.setText(sHour + ":" + sMinute);
                            }

                        }, hour, minutes, true);
                picker.show();
            }
        });

        final Spinner pickTheDay =findViewById(R.id.pickDay);
        String [] dayList = new String[]{"Enter the Day","Mar 4 2019","Mar 5 2019","Mar 6 2019","Mar 7 2019"};
        final Spinner pickTheBus = findViewById(R.id.pickTheBus);
        String[] busList = new String[]{
                "Enter Bus",
                "021 PLATEIA KANIGGOS - GKYZI(2484)",
                "022 MARASLEIOS - NEA KIPSELI(1804)",
                "022 NEA KIPSELI - MARASLEIOS(1805)",
                "024 AG. ANARGYROI - ST. ???? PATHSIA(2640),IPPOKRATOUS - PROFITI DANIIL(1797)",
                "025 PROFITI DANIIL - IPPOKRATOUS(1798)",
                "026 IPPOKRATOUS - VOTANIKOS(1799)",
                "026 VOTANIKOS - IPPOKRATOUS(1800)",
                "027 ORFEOS - IPPOKRATOUS(1975)",
                "027 IPPOKRATOUS - ORFEOS(1976)",
                "032 GOUDI-MARASLEIOS(2780)",
                "032 MARASLEIOS-GOUDI(2781)",
                "035 ANO KYPSELI - PETRALONA -TAVROS [TILL 06:30 & FROM 18:00](2948)",
                "035 TAVROS-PETRALONA - ANO KIPSELI,(2949)",
                "035 ANO KYPSELI - PETRALONA -TAVROS,(2953)",
                "036 ST. KATECHAKI - PANORMOU - GALATSI - KIPSELI[Every Tuesday during the farmer's market](2597)",
                "036 ST. KATECHAKI - PANORMOU - GALATSI - KIPSELI(2893)",
                "036 ST. KATECHAKI - PANORMOU - GALATSI - KIPSELI [TO PL. KIPSELI](2895)",
                "036 KIPSELI - GALATSI - ST. PANORMOU - ST.KATECHAKI [FROM PL. KIPSELI](2751)",
                "036 KIPSELI - GALATSI - ST. PANORMOU - ST.KATECHAKI [FROM PL. KIPSELI-Every Tuesday](2756)",
                "036 PEIRAIAS - SYNTAGMA(2005)",
                "040 SYNTAGMA - PEIRAIAS(2006)",
                "046 MOUSEIO - ELLINOROSON(1820)",
                "046 ELLINOROSON - MOYSEIO(1821)",
                "046 ELLINOROSON - MOYSEIO [TILL 06:45 & FROM 17:30](2954)",
                "049 OMONOIA - PEIRAIAS(1802)",
                "049 PEIRAIAS - OMONOIA(2995)",
                "051 OMONOIA-ST. YPER. LEOF. KHFISOU [temporary detour due to roadworks](3068)",
                "054 PERISSOS - AKADIMIA - METAMORFOSI(1876)",
                "054 METAMORFOSI - AKADIMIA - PERISSOS(1877)",
                "057 OMONOIA - LOFOS SKOUZE(1810)",
                "060 MOUSEIO - LYKAVITTOS(2990)",
                "060 MOUSEIO - LYKAVITTOS  [TILL 06:40 & FROM 18:00](2991)",
                "1 PLATEIA ATTIKIS - KALLITHEA - MOSCHATO(2018)",
                "1 MOSCHATO - KALLITHEA - PLATIA ATTIKIS(2019)",
                "10 TZITZIFIES - CHALANDRI(1993)",
                "10 CHALANDRI - TZITZIFIES(1994)"};
        ArrayAdapter<String> adapterBus = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, busList);
        ArrayAdapter<String> adapterDays = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dayList);

        pickTheBus.setAdapter(adapterBus);
        pickTheDay.setAdapter(adapterDays);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info =pickTheBus.getSelectedItem().toString();
                String day =pickTheDay.getSelectedItem().toString();

                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(info,requiredHour,requiredMin,day);
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        final LatLng athens =new LatLng(37.983810,23.727539);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(athens));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 3000, null);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    // Sub's main method in here
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private int subscriberId=1;


        private Socket clientSocket;
        private BufferedReader inputStream;

        private int port1=1111;
        private int port2=1112;

        private String host1="172.16.2.33";
        private String host2="172.16.2.32";
        private PrintWriter outputStream;

        public boolean isSent=false;


        @Override
        protected String doInBackground(String... params) {
             requiredBusId =params[0].split(" ")[0];
             requiredRouteCode=params[0].split("\\(")[1].replace(")","");

             requiredTime =convertToLocaTime(params[1],params[2]);
             requiredDay=params[3].split(" ")[1];

                connect(port1,requiredBusId,host1);
                pull();
                disconnect();
                if (!isSent) {
                    connect(port2,requiredBusId,host2);
                    pull();
                    disconnect();
                }

            return null;

        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }

        public void connect(int port,String topic,String host) {
            System.out.println("Starting Consumer on Thread: " + Thread.currentThread().getName());
            try {
                this.clientSocket = new Socket(host, port);
                this.outputStream = new PrintWriter(clientSocket.getOutputStream(), true);

                this.inputStream =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                registerToBroker(topic, subscriberId+"");


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void registerToBroker(String topic, String publisherId) {
            outputStream.println("Connection Request");
            outputStream.println(topic + "," + publisherId + "," + 1);
        }

        public void pull() {
            String receivedMessage ;
            try {
                while ((receivedMessage = inputStream.readLine()) != null) {
                    isSent = true;
                    parseIncomingMessage(receivedMessage);
                    busTime=convertToLocaTime(timestampOfBusPosition);
                        if(requiredRouteCode.equals(routeCode)&&busTime.isAfter(requiredTime)&&requiredDay.equals(timestampOfBusPosition.split(" ")[2])) {
                            UpdateMap(latitude, longitude);

                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void disconnect() {
            try {
                inputStream.close();
                outputStream.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void UpdateMap(double lat,double lon) {


        final LatLng point =new LatLng(lat,lon);

        markerOptions.position(point);
        markerOptions.title(lineId+","+routeCode+timestampOfBusPosition);
        markerOptions.snippet("test");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMap.addMarker(markerOptions);
            }
        });
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public LocalTime convertToLocaTime(String hour,String min){
        LocalTime localTime=LocalTime.now();
        LocalTime time=localTime.of(Integer.valueOf(hour),Integer.valueOf(min),0,0);

        return time;
    }

    public LocalTime convertToLocaTime (String timestampOfBusPosition){
        LocalTime localTime=LocalTime.now();
        LocalTime time;
        String [] timeInfo =timestampOfBusPosition.split(" ")[4].split(":");
        int hour=Integer.valueOf(timeInfo[0]);
        int min=Integer.valueOf(timeInfo[1]);
        if(timeInfo[3].contains("AM")){
            time=localTime.of(hour,min,0,0);
        }else{
            if(hour==24) time=localTime.of(00+min,0,0);
            else if (hour==12)  time=localTime.of(hour,min,0,0);
            else time=localTime.of(hour+12,min,0,0);
        }
        return time;
    }


    private void parseIncomingMessage(String input) {
        String[] lines = input.split("\\{");

          extractBusPosition(lines[2].split("\\},")[0]);
          extractRouteCode(lines[3].split("\\},")[0]);
          extractBusLine(lines[4].split("\\},")[0]);
    }

    private void extractBusPosition(String busPositionString) {
        String[] split = busPositionString.split(",");
         lineCode = split[0].split("='")[1].replace("'", "");
         routeCode = split[1].split("='")[1].replace("'", "");
         vehicleId = split[2].split("='")[1].replace("'", "");
         latitude = Double.parseDouble(split[3].split("='")[1].replace("'", ""));
         longitude = Double.parseDouble(split[4].split("='")[1].replace("'", ""));
         timestampOfBusPosition = split[5].split("='")[1].replace("'", "");

    }

    private void extractRouteCode(String routeCodeString) {
        String[] split = routeCodeString.split(",");

         routeCode = split[0].split("='")[1].replace("'","");
         routeType = Integer.parseInt(split[1].split("='")[1].replace("'",""));
         description = split[2].split("='")[1].replace("'","");

    }

    private void extractBusLine(String busLineString) {
        String[] split = busLineString.split(",");

         lineCode = split[0].split("='")[1].replace("'", "");
         lineId = split[1].split("='")[1].replace("'", "");
         description = split[2].split("='")[1].replace("}}'}}", "");

    }
}

