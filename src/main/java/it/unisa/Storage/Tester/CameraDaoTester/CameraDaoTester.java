//package it.unisa.Storage.Tester.CameraDaoTester;
//
//import it.unisa.Common.Camera;
//import it.unisa.Server.persistent.util.Stato;
//import it.unisa.Storage.DAO.CameraDAO;
//
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class CameraDaoTester{
//
//    private CameraDAO cameraDAO;
//    private int numeroCamera;
//    private Stato statoCamera;
//    private int capacità;
//    private double prezzoCamera;
//    private String noteCamera;
//
//    @BeforeEach
//    public void initTest(){
//        cameraDAO = new CameraDAO();
//    }
//
//    @AfterTest
//    public void afterTest(){
//        cameraDAO = null;
//    }
//
//    @BeforeEach
//    public void doSaveGoSuccessTest(){
//        numeroCamera = 110;
//        statoCamera = Stato.Libera;
//        capacità = 2;
//        prezzoCamera = 80;
//        noteCamera = "il cliente è celliaco";
//        try {
//            cameraDAO.doSave(new Camera(numeroCamera,statoCamera,capacità,prezzoCamera,noteCamera));
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Test
//    public void doDeleteGoSuccessTest(){
//        numeroCamera = 110;
//        statoCamera = Stato.Libera;
//        capacità = 2;
//        prezzoCamera = 80;
//        noteCamera = "il cliente è celliaco";
//
//        try {
//            cameraDAO.doDelete(new Camera(numeroCamera,statoCamera,capacità,prezzoCamera,noteCamera));
//        } catch (SQLException ex) {
//            throw new RuntimeException(ex);
//        }
//    }
//
//    @Test
//    public void doDeleteGoFailedTest(){
//            try {
//                cameraDAO.doDelete(null);
//            }catch (SQLException e){
//                e.printStackTrace();
//            }
//    }
//
//    @Test
//    public void doRetriveAllDescTest(){
//        String order = "decrescente";
//        try{
//            ArrayList<Camera> cameras = (ArrayList<Camera>) cameraDAO.doRetriveAll(order);
//            //AssertJUnit.assertEquals();
//        }catch(SQLException sqlException){
//            sqlException.printStackTrace();
//        }
//    }
//
//    public void doRetriveAllAsc(){
//
//    }
//}
