/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mainProgram;

import java.awt.Desktop;
import java.io.File;
import threads.CraftsmanThread;
import threads.CustomerThread;
import threads.EntrepreneurThread;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import log.interfaces.StorageLogInterface;
import log.interfaces.StoreLogInterface;
import log.interfaces.WorkshopLogInterface;
import storage.interfaces.EntrepreneurStorageInterface;
import store.intefaces.CustomerStoreInterface;
import store.intefaces.EntrepreneurStoreInterface;
import store.intefaces.WorkshopStoreInterface;
import workshop.interfaces.CraftsmanWorkshopInterface;
import workshop.interfaces.EntrepreneurWorkshopInterface;
import log.LogMonitor;
import storage.StorageMonitor;
import store.StoreMonitor;
import workshop.WorkshopMonitor;

/**
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

    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner sc = new Scanner(System.in);
        
        String selection;
        
        System.out.println("Bem vindo a simulacao de Aveiro Handicraft SARL");
        
        do{
            System.out.println("Selecione a opcao que deseja: ");

            do{
                System.out.println("    1 - Carregar ficheiro de configuracao e correr simulacao");
                System.out.println("    2 - Introduzir parametros e correr simulacao");
                System.out.println("    3 - Alterar ficheiro de configuracao");
                System.out.println("    4 - Fazer (n) simulacoes em sequencia");
                System.out.println("    5 - Mostrar ficheiro de configuracao");
                System.out.println("    6 - Terminar simulacao");
                System.out.print("    Entrada: ");
                selection = sc.nextLine();
            }while(!selection.equals("1") && !selection.equals("2") && !selection.equals("3")
                    && !selection.equals("4") && !selection.equals("5") && !selection.equals("6"));

            switch(selection){
                case "1":
                    System.out.println("Verificando se existe um ficheiro de configuracao...");
                    boolean exists = Files.exists(Paths.get("./src/mainProgram/", "running_config"));
                    if(exists){
                        System.out.println("Ficheiro de configuracao encontrado com sucesso");
                        System.out.println("Conteudo do ficheiro de configuracao e:");
                        List<String> readAllLines = Files.readAllLines(Paths.get("./src/mainProgram/", "running_config"));
                        List<String> fileStringList = Files.readAllLines(Paths.get("./src/mainProgram/", "running_config"));
                        Iterator<String> iterator = readAllLines.iterator();
                        while(iterator.hasNext()){
                            System.out.println("    " + iterator.next());
                            iterator.remove();
                        }
                        System.out.println();
                        do{
                            System.out.print("Deseja correr a simulacao?(s/n): ");
                            selection = sc.nextLine();
                        }while(!selection.equals("s") && !selection.equals("n"));
                        if(selection.equals("s")){
                            MainProgram.parseFileForSimulation(fileStringList);
                            MainProgram.runSimulation();
                            do {
                                System.out.print("Deseja abrir o relatorio(s/n): ");
                                selection = sc.nextLine();
                            } while (!selection.equals("s") && !selection.equals("n"));
                            if(selection.equals("s"))
                                MainProgram.editFile(Paths.get("./src/mainProgram/", "running_config").toFile());
                        }
                        else
                            System.out.println("Abortando a simulacao...");
                    }
                    else{
                        System.out.println("Ficheiro de configuracao nao foi encontrado");
                        do{
                            System.out.print("Deseja criar ficheiro de configuracao?(s/n): ");
                            selection = sc.nextLine();
                        }while(!selection.equals("s") && !selection.equals("n"));
                        if(selection.equals("s")){
                            MainProgram.readSimulationParametersFromUser();
                            MainProgram.writeConfFile();
                            System.out.println("Ficheiro de configuracao criado com sucesso");
                        }
                        else
                            System.out.println("Abortando a criacao de um ficheiro de configuracao...");
                    }
                    break;
                case "2":
                    MainProgram.readSimulationParametersFromUser();
                    MainProgram.runSimulation();
                    break;
                case "3":
                    System.out.println("Conteudo do ficheiro de configuracao e:");
                    List<String> readAllLines = Files.readAllLines(Paths.get("./src/mainProgram/", "running_config"));
                    Iterator<String> iterator = readAllLines.iterator();
                    while(iterator.hasNext()){
                        System.out.println("    " + iterator.next());
                        iterator.remove();
                    }
                    MainProgram.readSimulationParametersFromUser();
                    MainProgram.writeConfFile();
                    break;
                case "4":
                    boolean badInput;
                    int interations = 0;
                    int i = 1;
                    do {
                        System.out.print("Introduza o numero de vezes que quer correr a simulacao: ");
                        try {
                            interations = Integer.parseInt(sc.nextLine());
                            if (interations <= 0) {
                                badInput = true;
                            } else {
                                MainProgram.stockPrimeMaterials = interations;
                                badInput = false;
                            }
                        } catch (NumberFormatException e) {
                            badInput = true;
                        }
                    } while (badInput);
                    
                    try{
                    MainProgram.parseFileForSimulation(Files.readAllLines(Paths.get("./src/mainProgram/", "running_config")));
                    }
                    catch(NoSuchFileException ex){
                        System.out.println("Nao foi encontrado um ficheiro de configuracao, abortando a simulacao...");
                        break;
                    }
                    do{
                        MainProgram.runSimulation();
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("Simulacao concluida, passagem numero: " + i);
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------");
                        
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        
                    }while(i++ < interations);
                    break;
                case "5":
                    exists = Files.exists(Paths.get("./src/mainProgram/", "running_config"));
                    if (exists) {
                        System.out.println("Ficheiro de configuracao encontrado com sucesso");
                        System.out.println("Conteudo do ficheiro de configuracao e:");
                        readAllLines = Files.readAllLines(Paths.get("./src/mainProgram/", "running_config"));
                        iterator = readAllLines.iterator();
                        while (iterator.hasNext()) {
                            System.out.println("    " + iterator.next());
                            iterator.remove();
                        }
                        System.out.println();
                    }
                    else{
                        System.out.println("Ficheiro de configuracao nao foi encontrado");
                        do {
                            System.out.print("Deseja criar ficheiro de configuracao?(s/n): ");
                            selection = sc.nextLine();
                        } while (!selection.equals("s") && !selection.equals("n"));
                        if (selection.equals("s")) {
                            MainProgram.readSimulationParametersFromUser();
                            MainProgram.writeConfFile();
                            System.out.println("Ficheiro de configuracao criado com sucesso");
                        } else {
                            System.out.println("Abortando a criacao de um ficheiro de configuracao...");
                        }
                    }
                    break;
                case "6":
                    System.out.println("Terminando a simulacao...");
                    System.out.println("Obrigado!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Terminando a simulacao...");
                    System.out.println("Obrigado!");
                    System.exit(0);
                    break;
            }
            System.out.println("Sucesso!!");
        }while(true);   
    }
    
    public static void runSimulation() throws InterruptedException{
        CraftsmanThread[] craftsman = new CraftsmanThread[MainProgram.howManyCraftsmanThreads];
        CustomerThread[] customer = new CustomerThread[MainProgram.howManyCustomers];
        
        
        LogMonitor logMonitor = new LogMonitor(MainProgram.howManyCustomers, MainProgram.stockPrimeMaterials,
        MainProgram.howManyCraftsmanThreads, MainProgram.fileName, MainProgram.filePath, 3);

        StorageMonitor storageMonitor = new StorageMonitor(MainProgram.stockPrimeMaterials, 
                (StorageLogInterface)logMonitor);
        
        
        StoreMonitor storeMonitor = new StoreMonitor((StoreLogInterface)logMonitor, MainProgram.howManyCustomers);
        
        
        WorkshopMonitor workshopMonitor = new WorkshopMonitor(MainProgram.productThreshold, 
                (WorkshopLogInterface)logMonitor, (WorkshopStoreInterface)storeMonitor, MainProgram.howManyCraftsmanThreads);

        
        EntrepreneurThread entrepreneurThread = new EntrepreneurThread(0, MainProgram.amountProductsToPickUp, 
                MainProgram.amountOfReplenishWorkshop, (EntrepreneurStorageInterface)storageMonitor, 
                (EntrepreneurStoreInterface)storeMonitor,
                (EntrepreneurWorkshopInterface)workshopMonitor);
        
        
        for(int i = 0; i < MainProgram.howManyCraftsmanThreads; i++){
            craftsman[i] = new CraftsmanThread(i, MainProgram.productStep, 
                    MainProgram.amountToCollectPrimeMaterialsCraftsman, 
                    (CraftsmanWorkshopInterface)workshopMonitor, 
                    (WorkshopStoreInterface)storeMonitor, MainProgram.elapsedTime);
        }
        
        for(int i = 0; i < MainProgram.howManyCustomers; i++){
            customer[i] = new CustomerThread(i, (CustomerStoreInterface)storeMonitor);
        }
        
        
        entrepreneurThread.start();
        
        for(int i = 0; i < MainProgram.howManyCraftsmanThreads; i++){
            craftsman[i].start();
        }
        
        for(int i = 0; i < MainProgram.howManyCustomers; i++){
            customer[i].start();
        }
        
        for(int i = 0; i < MainProgram.howManyCraftsmanThreads; i++){
            craftsman[i].join();
        }
        
        for(int i = 0; i < MainProgram.howManyCustomers; i++){
            customer[i].join();
        }
        
        entrepreneurThread.join();
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
        
        
        
        Files.write(Paths.get("./src/mainProgram/", "running_config"), list);
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
}
