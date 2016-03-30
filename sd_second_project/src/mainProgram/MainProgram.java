/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;

import genclass.GenericIO;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main Program.
 * 
 * @author Luis Assunção 
 * @author Joel Pinheiro
 */
public class MainProgram {
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
    
    private static int portNumbStorage = -1;
    private static int portNumbStore = -1;
    private static int portNumbLog = -1;
    private static int portNumbWorkshop = -1;
    private static String serverUrlStorage = null;
    private static String serverUrlStore = null;
    private static String serverUrlWorkshop = null;
    private static String serverUrlLog = null;
    private static boolean allConfParametersReady = false;

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException InterruptedException
     * @throws java.io.IOException IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        String selection;
        
        final int portNumbMain = 22279;
        
        System.out.println("Server storage host address: localhost:" + portNumbMain);
        
        // server logic
        ServerCom scon, sconi;

        scon = new ServerCom(portNumbMain);
        scon.start();

        GenericIO.writelnString("O serviço foi estabelecido!");
        GenericIO.writelnString("O servidor esta em escuta.");
        
        System.out.println("Bem vindo a simulacao de Aveiro Handicraft SARL");
        
        System.out.println("Ficheiro de configuracao encontrado com sucesso");
        System.out.println("Conteudo do ficheiro de configuracao e:");
        List<String> readAllLines = Files.readAllLines(Paths.get(".", "running_config"));
        List<String> fileStringList = Files.readAllLines(Paths.get(".", "running_config"));
        Iterator<String> iterator = readAllLines.iterator();
        while (iterator.hasNext()) {
            System.out.println("    " + iterator.next());
            iterator.remove();
        }
        do {
            System.out.print("Deseja correr a simulacao?(s/n): ");
            selection = sc.nextLine();
        } while (!selection.equals("s") && !selection.equals("n"));
        if (selection.equals("s")) {
            MainProgram.parseFileForSimulation(fileStringList);

            // now we have the sym parameters, we must distribute them

            while (true) {
                sconi = scon.accept();                                                  // entrada em processo de escuta
                //proxy = new WorkshopProxy(sconi, workshopBoundary);         // lançamento do agente prestador do serviço
                //proxy.start();
                new MainServerProxy(sconi).start();
            }
        }
    }

    private static void writeConfFile() throws IOException {
        List<String> list = new ArrayList<>();
        list.add(fileName);
        list.add(filePath);
        list.add("Quantidade materias primas -> " + stockPrimeMaterials);
        list.add("Limite de produtos na oficina para avisar a dona (recolha) -> " + productThreshold);
        list.add("Quantidade de produtos que a dona pode levar para a loja de cada vez -> " + amountProductsToPickUp);
        list.add("Quantidade de materias primas que a dona pode levar para a oficina de cada vez -> " + amountOfReplenishWorkshop);
        list.add("Quantos artesaos existem -> " + howManyCraftsmanThreads);
        list.add("Quantos clientes existem -> " + howManyCustomers);
        list.add("Quantos produtos os artesaos fabricam por cada materia prima -> " + productStep);
        list.add("Quantas materias primas levam os artesaos para a sua mesa de trabalho (todas sao consumidas para produzir n produtos) -> " + amountToCollectPrimeMaterialsCraftsman);
        list.add("Quanto tempo(ms) demora a produzir x pecas -> " + elapsedTime);
        
        
        
        Files.write(Paths.get(".", "running_config"), list);
    }

    private static void parseFileForSimulation(List<String> content) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m;
        int values[] = new int[9];
        int i = 0;
        String line;
        
        MainProgram.fileName = content.get(0);
        MainProgram.filePath = content.get(1);
        content.remove(0);
        content.remove(0);
        
        ListIterator<String> listIterator = content.listIterator();
        
        while(listIterator.hasNext()){
            line = listIterator.next();
            m = p.matcher(line);
            m.find();
            values[i++] = Integer.parseInt(m.group());
        }
        
        MainProgram.stockPrimeMaterials = values[0];
        MainProgram.productThreshold = values[1];

        MainProgram.amountProductsToPickUp = values[2];
        MainProgram.amountOfReplenishWorkshop = values[3];

        MainProgram.howManyCraftsmanThreads = values[4];
        MainProgram.howManyCustomers = values[5];
        MainProgram.productStep = values[6];
        MainProgram.amountToCollectPrimeMaterialsCraftsman = values[7];
        MainProgram.elapsedTime = values[8];
    }

    private static void readSimulationParametersFromUser() {
        Scanner sc = new Scanner(System.in);
        boolean badInput;
        String literalValue;
        int numericValue;
                    
        do{
            System.out.print("Introduza o nome do ficheiro de relatorio: ");
            literalValue = sc.nextLine();
            if(literalValue.equals(""))
                badInput = true;
            else{
                MainProgram.fileName = literalValue;
                badInput = false;
            }
        } while (badInput);

        do {
            System.out.print("Introduza o caminho do ficheiro de relatorio: ");
            literalValue = sc.nextLine();
            if (literalValue.equals("")) {
                badInput = true;
            } else {
                MainProgram.filePath = literalValue;
                badInput = false;
            }
        } while (badInput);

        do {
            System.out.print("Introduza a quantidade de materias primas em stock no armazem: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.stockPrimeMaterials = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza a quantidade de produtos que a dona pode trazer da oficina: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.amountProductsToPickUp = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza a quantidade de artesoes que vao trabalhar na oficina: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.howManyCraftsmanThreads = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza o limite maximo de produtos produzidos para os artesaos efetuarem a chamada de recolha de produtos: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.productThreshold = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza a quantidade de produtos que os artesaos fabricam por cada materia prima: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.productStep = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza a quantidade de materia prima que a dona pode transportar para a oficina de cada vez: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.amountOfReplenishWorkshop = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza a quantidade de materia prima que o artesao pode transportar para a sua mesa de trabalho (todas sao consumidas para produzir n produtos): ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.amountToCollectPrimeMaterialsCraftsman = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza quanto tempo(ms) o artesao demora a produzir " + MainProgram.productStep + " produtos de cada vez: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue < 0) {
                    badInput = true;
                } else {
                    MainProgram.elapsedTime = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);

        do {
            System.out.print("Introduza o numeros de clientes que podem visitar a loja: ");
            try {
                numericValue = Integer.parseInt(sc.nextLine());
                if (numericValue <= 0) {
                    badInput = true;
                } else {
                    MainProgram.howManyCustomers = numericValue;
                    badInput = false;
                }
            } catch (NumberFormatException e) {
                badInput = true;
            }
        } while (badInput);
    }
    
    /**
     *
     * @param file file
     */
    public static void editFile(final File file) {
        if (!Desktop.isDesktopSupported()) {
            System.out.println("Erro: GUI nao esta corretamente integrada com Java, impossivel abrir o ficheiro");
            return;
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.EDIT)) {
            System.out.println("Erro: GUI nao esta corretamente integrada com Java, impossivel abrir o ficheiro");
            return;
        }

        try {
            desktop.edit(file);
        } catch (IOException e) {
            // Log an error

        }
    }

    /**
     *
     * @return return result getFileName
     */
    public static String getFileName() {
        return fileName;
    }

    /**
     *
     * @return return result getFilePath
     */
    public static String getFilePath() {
        return filePath;
    }

    /**
     *
     * @return return result getStockPrimeMaterials
     */
    public static int getStockPrimeMaterials() {
        return stockPrimeMaterials;
    }

    /**
     *
     * @return return result getProductThreshold
     */
    public static int getProductThreshold() {
        return productThreshold;
    }

    /**
     *
     * @return return result getAmountProductsToPickUp
     */
    public static int getAmountProductsToPickUp() {
        return amountProductsToPickUp;
    }

    /**
     *
     * @return return result getAmountOfReplenishWorkshop
     */
    public static int getAmountOfReplenishWorkshop() {
        return amountOfReplenishWorkshop;
    }

    /**
     *
     * @return return result getHowManyCraftsmanThreads
     */
    public static int getHowManyCraftsmanThreads() {
        return howManyCraftsmanThreads;
    }

    /**
     *
     * @return return result getHowManyCustomers
     */
    public static int getHowManyCustomers() {
        return howManyCustomers;
    }

    /**
     *
     * @return return result getProductStep
     */
    public static int getProductStep() {
        return productStep;
    }

    /**
     *
     * @return return result getAmountToCollectPrimeMaterialsCraftsman
     */
    public static int getAmountToCollectPrimeMaterialsCraftsman() {
        return amountToCollectPrimeMaterialsCraftsman;
    }

    /**
     *
     * @return return result
     */
    public static int getElapsedTime() {
        return elapsedTime;
    }

    /**
     *
     * @return return result
     */
    public static int getPortNumbStorage() {
        return portNumbStorage;
    }

    /**
     *
     * @param portNumbStorage portNumbStorage
     */
    public static void setPortNumbStorage(int portNumbStorage) {
        MainProgram.portNumbStorage = portNumbStorage;
    }

    /**
     *
     * @return return result
     */
    public static int getPortNumbStore() {
        return portNumbStore;
    }

    /**
     *
     * @param portNumbStore portNumbStore
     */
    public static void setPortNumbStore(int portNumbStore) {
        MainProgram.portNumbStore = portNumbStore;
    }

    /**
     *
     * @return return result
     */
    public static int getPortNumbLog() {
        return portNumbLog;
    }

    /**
     *
     * @param portNumbLog portNumbLog
     */
    public static void setPortNumbLog(int portNumbLog) {
        MainProgram.portNumbLog = portNumbLog;
    }

    /**
     *
     * @return return result
     */
    public static int getPortNumbWorkshop() {
        return portNumbWorkshop;
    }

    /**
     *
     * @param portNumbWorkshop portNumbWorkshop
     */
    public static void setPortNumbWorkshop(int portNumbWorkshop) {
        MainProgram.portNumbWorkshop = portNumbWorkshop;
    }

    /**
     *
     * @return return result
     */
    public static String getServerUrlStorage() {
        return serverUrlStorage;
    }

    /**
     *
     * @param serverUrlStorage serverUrlStorage
     */
    public static void setServerUrlStorage(String serverUrlStorage) {
        MainProgram.serverUrlStorage = serverUrlStorage;
    }

    /**
     *
     * @return return result
     */
    public static String getServerUrlStore() {
        return serverUrlStore;
    }

    /**
     *
     * @param serverUrlStore serverUrlStore
     */
    public static void setServerUrlStore(String serverUrlStore) {
        MainProgram.serverUrlStore = serverUrlStore;
    }

    /**
     *
     * @return return result
     */
    public static String getServerUrlWorkshop() {
        return serverUrlWorkshop;
    }

    /**
     *
     * @param serverUrlWorkshop serverUrlWorkshop
     */
    public static void setServerUrlWorkshop(String serverUrlWorkshop) {
        MainProgram.serverUrlWorkshop = serverUrlWorkshop;
    }

    /**
     *
     * @return return result
     */
    public static String getServerUrlLog() {
        return serverUrlLog;
    }

    /**
     *
     * @param serverUrlLog serverUrlLog
     */
    public static void setServerUrlLog(String serverUrlLog) {
        MainProgram.serverUrlLog = serverUrlLog;
    }

    /**
     *
     * @return return result
     */
    public static boolean allConfParametersCollected() {
        return allConfParametersReady;
    }
    
    /**
     *
     * @param value value
     */
    public static void setAllConfParametersReady(boolean value){
        MainProgram.allConfParametersReady = value;
    }

    static String getLogFileName() {
        return MainProgram.fileName;
    }

    static String getLogFilePath() {
        return MainProgram.filePath;
    }
}   
