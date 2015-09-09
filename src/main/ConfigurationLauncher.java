/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import craftsman.thread.ClientCraftsman;
import customer.thread.ClientCustomers;
import entrepreneur.thread.ClientEntrepreneur;
import genclass.GenericIO;
import interfaces.ConfigurationInterface;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import log.monitor.ServerLogMonitor;
import storage.monitor.ServerStorageMonitor;
import store.monitor.ServerStoreMonitor;
import workshop.monitor.ServerWorkshopMonitor;

/**
 *
 * @author luis
 */
public class ConfigurationLauncher {

    private static String fileName;
    private static String filePath;
    private static int stockPrimeMaterials;
    private static int productThreshold;

    private static int amountProductsToPickUp;
    private static int amountOfReplenishWorkshop;

    private static int howManyCraftsmanThreads;
    private static int howManyCustomers;
    private static int productStep;
    private static int amountToCollectPrimeMaterialsCraftsman;
    private static int elapsedTime;
    private static Object instance;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Process rmiRegistry = null;
        String input;
        String rmiRegHostName;
        int rmiRegPortNumb;
        String nameEntryConfigurationRecord = "configurationRecord";
        Scanner sc = new Scanner(System.in);

        do {
            System.out.println("Selecione uma opcao");
            System.out.println("    (0) -> Verificar Configuracoes");
            System.out.println("    (1) -> Iniciar servidor RMI");
            System.out.println("    (2) -> Iniciar servidor Registo");
            System.out.println("    (3) -> Iniciar servidor Log");
            System.out.println("    (4) -> Iniciar servidor Storage");
            System.out.println("    (5) -> Iniciar servidor Store");
            System.out.println("    (6) -> Iniciar servidor Workshop");
            System.out.println("    (7) -> Iniciar cliente Entrepreneur");
            System.out.println("    (8) -> Iniciar cliente Customers");
            System.out.println("    (9) -> Iniciar cliente Craftsmen");
            System.out.println("    (10) -> Terminar Execucao");
            System.out.print("Valor: ");
            input = sc.nextLine();
            if (input.equals("0")) {
                List<String> readAllLines = null;
                try {
                    readAllLines = Files.readAllLines(Paths.get(".", "running_config"));
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                    System.exit(1);
                }
                Iterator<String> iterator = readAllLines.iterator();
                while (iterator.hasNext()) {
                    System.out.println("    " + iterator.next());
                    iterator.remove();
                }
            } else if (input.equals("1")) {
                InetAddress IP = null;
                try {
                    IP = InetAddress.getLocalHost();
                } catch (UnknownHostException ex) {
                    System.out.println(ex.toString());
                    System.exit(1);
                }
                rmiRegHostName = IP.getHostAddress();
                rmiRegPortNumb = 22270;

                //System.setProperty("java.rmi.server.hostname", String.valueOf(IP.getHostAddress()));
                //System.out.println(String.valueOf(IP.getHostAddress()));
                //"J-Djava.rmi.server.hostname="+IP.getHostAddress(),
                try {
                    ProcessBuilder proc = new ProcessBuilder("rmiregistry", "-J-Djava.rmi.server.codebase=http://" + rmiRegHostName + "/" + System.getProperty("user.name") + "/P2_TP2_G7.jar", "-J-Djava.rmi.server.useCodebaseOnly=true", "22270");
                    rmiRegistry = proc.start();
                    System.out.println("The rmi registry server has been started on: " + IP.toString() + ", and the http server is being hosted on http://" + rmiRegHostName + "/" + System.getProperty("user.name") + "/P2_TP2_G7.jar");
                    Class<?> ProcessImpl = rmiRegistry.getClass();
                    Field field = ProcessImpl.getDeclaredField("pid");
                    field.setAccessible(true);
                    System.out.println("RMI registry server pid is " + field.getInt(rmiRegistry));
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                    System.exit(1);
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.toString());
                    System.exit(1);
                } catch (IllegalAccessException ex) {
                    System.out.println(ex.toString());
                    System.exit(1);
                } catch (NoSuchFieldException ex) {
                    System.out.println(ex.toString());
                    System.exit(1);
                } catch (SecurityException ex) {
                    System.out.println(ex.toString());
                    System.exit(1);
                }

                try {
                    List<String> fileStringList = Files.readAllLines(Paths.get(".", "running_config"));
                    ConfigurationLauncher.parseFileForSimulation(fileStringList);
                } catch (IOException ex) {
                    System.out.println(ex.toString());
                    System.exit(0);
                }
                ConfigurationInterface confInter = null;
                ConfigurationParameters cParameters = new ConfigurationParameters(fileName, filePath,
                        productThreshold, amountProductsToPickUp, amountOfReplenishWorkshop,
                        howManyCraftsmanThreads, howManyCustomers, productStep,
                        amountToCollectPrimeMaterialsCraftsman, stockPrimeMaterials, elapsedTime);
                int configurationPort = 22275;
                String nameEntryConfManager = "confManager";

                try {
                    confInter = (ConfigurationInterface) UnicastRemoteObject.exportObject(cParameters, configurationPort);
                } catch (RemoteException e) {
                    GenericIO.writelnString("Excepção na geração do stub para o ConfigurationManager: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                GenericIO.writelnString("O stub para o ConfigurationManager foi gerado!");

                System.setProperty("java.rmi.server.hostname", rmiRegHostName);

                /* seu registo no serviço de registo RMI */
                Registry registry = null;

                try {
                    registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
                } catch (RemoteException e) {
                    GenericIO.writelnString("Excepção na criação do registo RMI: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                GenericIO.writelnString("O registo RMI foi criado!");

                System.setProperty("java.rmi.server.hostname", rmiRegHostName);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }

                try {
                    registry.bind(nameEntryConfManager, confInter);
                } catch (RemoteException e) {
                    GenericIO.writelnString("Excepção no registry bind do ConfigurationManager: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                } catch (AlreadyBoundException e) {
                    GenericIO.writelnString("O ConfigurationManager já está registado: " + e.getMessage());
                    e.printStackTrace();
                    System.exit(1);
                }
                GenericIO.writelnString("O ConfigurationManager foi registado!");
            } else if(input.equals("2")){
                utils.ServerRegisterRemoteObject rObject = new utils.ServerRegisterRemoteObject();
                ConfigurationLauncher.instance = rObject;
            } else if (input.equals("3")) {
                log.monitor.ServerLogMonitor lMonitor = new ServerLogMonitor("");
                ConfigurationLauncher.instance = lMonitor;
            } else if (input.equals("4")) {
                storage.monitor.ServerStorageMonitor sMonitor = new ServerStorageMonitor("");
                ConfigurationLauncher.instance = sMonitor;
            } else if (input.equals("5")) {
                store.monitor.ServerStoreMonitor sMonitor = new ServerStoreMonitor("");
                ConfigurationLauncher.instance = sMonitor;
            } else if (input.equals("6")) {
                workshop.monitor.ServerWorkshopMonitor wMonitor = new ServerWorkshopMonitor("");
                ConfigurationLauncher.instance = wMonitor;
            } else if (input.equals("7")) {
                entrepreneur.thread.ClientEntrepreneur cEntrepreneur = new ClientEntrepreneur("");
                ConfigurationLauncher.instance = cEntrepreneur;
            } else if (input.equals("8")) {
                customer.thread.ClientCustomers cCustomers = new ClientCustomers("");
                ConfigurationLauncher.instance = cCustomers;
            } else if (input.equals("9")) {
                craftsman.thread.ClientCraftsman cCraftsman = new ClientCraftsman("");
                ConfigurationLauncher.instance = cCraftsman;
            } else {
                if (input.equals("10")) {
                    System.out.println("Saindo...");
                } else {
                    System.out.println("Comando nao reconhecido");
                }
            }
        } while (!input.equals("10"));

        if (rmiRegistry != null) {
            // this proces initialized the rmi, we should terminate the server now
            rmiRegistry.destroy();
            System.out.println("RMI registry server stopped with success");
            System.exit(0);
        } else {
            try {
                ConfigurationLauncher.instance.getClass().getMethod("Terminate", (Class<?>[]) null).invoke(ConfigurationLauncher.instance, (Object) null);
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                System.out.println(ex.toString());
            }
            System.exit(0);
        }

        // here we launch the configurations a processes selectively
        // configuration now resides on a object, and is the task specification. we may read it from a file and commit to the object
            /*
         Programa principal
         rmiregistry -J-Djava.rmi.server.codebase="http://127.0.0.1/sd_third_project.jar" -J-Djava.rmi.server.useCodebaseOnly=true 22270
         java -Djava.security.policy=/home/luis/NetBeansProjects/sd_third_project/src/interfaces/policy.policy -Djava.rmi.server.useCodebaseOnly=true -cp -Djava.rmi.server.codebase="file:///home/luis/NetBeansProjects/sd_third_project/dist/sd_third_project.jar" log.monitor.ServerLogMonitor
             
         RmiRegistry Port -> 22270
         Http Port -> 80
         LogMonitor Port -> 22271
         StorageMonitor Port -> 22272
         StoreMonitor Port -> 22273
         WorkshopMonitor Port -> 22274
         Configuration Port -> 22275
         RegisterRemoteObject server -> 22276
         */
        /*
         rmiregistry -J-Djava.rmi.server.codebase="http://localhost/sd_third_project.jar" -J-Djava.rmi.server.useCodebaseOnly=true 22270
         java -Djava.security.policy=http://localhost/policy.policy -Djava.rmi.server.useCodebaseOnly=true -cp -Djava.rmi.server.codebase="file:///Users/joelpinheiro/NetBeansProjects/sd_third_project/sd_third_project/dist/sd_third_project.jar" log.monitor.ServerLogMonitor
        
        JOEL:
        java -Djava.security.policy=file:///Users/joelpinheiro/NetBeansProjects/sd_third_project/sd_third_project/policy.policy -Djava.rmi.server.useCodebaseOnly=true -cp -Djava.rmi.server.codebase="file:///Users/joelpinheiro/NetBeansProjects/sd_third_project/sd_third_project/dist/sd_third_project.jar" main.ConfigurationLauncher
        
        
        */
    }

    private static void parseFileForSimulation(List<String> content) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m;
        int values[] = new int[9];
        int i = 0;
        String line;

        ConfigurationLauncher.fileName = content.get(0);
        ConfigurationLauncher.filePath = content.get(1);
        content.remove(0);
        content.remove(0);

        ListIterator<String> listIterator = content.listIterator();

        while (listIterator.hasNext()) {
            line = listIterator.next();
            m = p.matcher(line);
            m.find();
            values[i++] = Integer.parseInt(m.group());
        }

        ConfigurationLauncher.stockPrimeMaterials = values[0];
        ConfigurationLauncher.productThreshold = values[1];

        ConfigurationLauncher.amountProductsToPickUp = values[2];
        ConfigurationLauncher.amountOfReplenishWorkshop = values[3];

        ConfigurationLauncher.howManyCraftsmanThreads = values[4];
        ConfigurationLauncher.howManyCustomers = values[5];
        ConfigurationLauncher.productStep = values[6];
        ConfigurationLauncher.amountToCollectPrimeMaterialsCraftsman = values[7];
        ConfigurationLauncher.elapsedTime = values[8];
    }
}
