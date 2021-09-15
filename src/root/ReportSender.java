public class ReportSender {

    public static void main(String[] args){
        int idCentralina = Integer.parseInt(args[0]); //id in new centralina: CHANGE THIS AS NEEDED!
        // int startValue = 1; //NO NEED FOR CHANGE: initial value in new centralina, needed for set start timer
        System.out.println("Centralina " + idCentralina + " Start...");

        // create the status to associate with centralina
        CentralinaStatus state = new CentralinaStatus(idCentralina);

        ReportCreator.createReportCreator(state); //create new centralina

        CentralinaConnector.getInstance();
        TimerManager.getInstance().updateTimerCounter(1); // new instance of timer
    }

    public static void sendReport(){
        System.out.println("I am sending a Report!");
        ReportCreator creator = ReportCreator.getInstance();
        CentralinaReport centralinaReportToSend = creator.createCentralinaReport(); //retrive new actual report

        //send report to central system
        CentralinaConnector.getInstance().sendCentralinaReport(centralinaReportToSend);

        TimerManager timerManager = TimerManager.getInstance();
        int traffic = centralinaReportToSend.getTrafficAmount();
        System.out.println(traffic); //print sent traffic value for montoring purposes
        timerManager.updateTimerCounter(traffic); //set new timer
    }
}
