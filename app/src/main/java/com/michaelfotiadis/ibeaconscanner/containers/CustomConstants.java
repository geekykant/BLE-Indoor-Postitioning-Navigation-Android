package com.michaelfotiadis.ibeaconscanner.containers;

import java.text.DecimalFormat;

public class CustomConstants {
    public static final String BLUETOOTH_DEVICE = "Bluetooth LE Device";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    public static final DecimalFormat df = new DecimalFormat("#.00");

    public enum Broadcasts {
        BROADCAST_1("Brodacast_1"),
        BROADCAST_2("Broadcast_2");
        
        private String text;

        private Broadcasts(String str) {
            this.text = str;
        }

        public String getString() {
            return this.text;
        }
    }

    public enum Payloads {
        PAYLOAD_1("Payload_1"),
        PAYLOAD_2("Payload_2"),
        PAYLOAD_3("Payload_3"),
        PAYLOAD_4("Payload_4"),
        PAYLOAD_5("Payload_5");
        
        private String text;

        private Payloads(String str) {
            this.text = str;
        }

        public String getString() {
            return this.text;
        }
    }

    public enum Requests {
        REQUEST_CODE_1(1),
        REQUEST_CODE_2(2),
        REQUEST_CODE_3(3);
        
        private int code;

        private Requests(int i) {
            this.code = i;
        }

        public int getCode() {
            return this.code;
        }
    }

    public enum Results {
        RESULT_1("Result_1"),
        RESULT_2("Result_2");
        
        private String text;

        private Results(String str) {
            this.text = str;
        }

        public String getString() {
            return this.text;
        }
    }
}
