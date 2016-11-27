package ch.viascom.groundwork.security.models;

import org.joda.time.DateTime;

import java.io.Serializable;

public class IPModel implements Serializable  {

        private String IP;
        private String description;
        private DateTime addDate;
        
        
        public String getIP() {
                return IP;
        }
        
        public void setIP(String IP) {
                this.IP = IP;
        }
        
        public String getDescription() {
                return description;
        }
        
        public void setDescription(String description) {
                this.description = description;
        }
        
        public DateTime getAddDate() {
                return addDate;
        }
        
        public void setAddDate(DateTime addDate) {
                this.addDate = addDate;
        }


        /**
         * Constructor.
         */
        public IPModel() {
                super();
        }
        
        /**
         * Constructor.
         * 
         * @param IP
         * @param description
         */
        public IPModel(String IP, String description) {
                super();
                this.IP = IP;
                this.description = description;
        }
        
        /**
         * Constructor.
         * 
         * @param IP
         * @param description
         * @param addDate
         */
        public IPModel(String IP, String description, DateTime addDate) {
                super();
                this.IP = IP;
                this.description = description;
                this.addDate = addDate;
        }
}