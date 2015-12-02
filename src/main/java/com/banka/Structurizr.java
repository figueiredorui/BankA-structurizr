package com.BankA;

import com.structurizr.Workspace;
import com.structurizr.api.StructurizrClient;
import com.structurizr.io.StructurizrWriterException;
import com.structurizr.io.json.JsonWriter;
import com.structurizr.model.*;
import com.structurizr.view.*;

import java.io.StringWriter;

/**
 * This is a simple example of how to get started with Structurizr for Java.
 */
public class Structurizr {

    public static void main(String[] args) throws Exception {
        Workspace workspace = new Workspace("BankA","This is a model of the system context for the BankA, the code for which can be found at https://github.com/figueiredorui/BankA");
        Model model = workspace.getModel();

        Person user = model.addPerson("User", "A user of my software system.");

        //SoftwareSystem banka = model.addSoftwareSystem("BankA", "App to import, categorize and analyse bank transactions");
        SoftwareSystem bankaWeb = model.addSoftwareSystem("BankA Web", "Web app to import, categorize and analyse bank transactions");
        SoftwareSystem bankaDesktop = model.addSoftwareSystem("BankA Desktop", "Desktop app to import, categorize and analyse bank transactions");

        // people

        user.uses(bankaWeb, "View transactions, reports,...");
        user.uses(bankaDesktop, "View transactions, reports,...");


        // Container bankaWeb
        Container webApp = bankaWeb.addContainer("Web Application","display transactions, reports,....","HTML 5 AngularJS");
        Container webApi = bankaWeb.addContainer("Web Api","api from transactions, reports,.....","ASP.NET API");
        Container webDatabase = bankaWeb.addContainer("Relational Database","Stores transactions, reports.","SQL SERVER / SQlite");

        user.uses(webApp,"");
        webApp.uses(webApi, "REST JSON");
        webApi.uses(webDatabase, "Reads from and writes data to");

        // Container bankaDesktop
        Container desktopApp = bankaDesktop.addContainer("Desktop Application","display transactions, reports,....","CEF");
        Container desktopDatabase = bankaDesktop.addContainer("Relational Database","Stores transactions, reports.","SQL SERVER / SQlite");

        user.uses(desktopApp,"");            
        desktopApp.uses(desktopDatabase, "Reads from and writes data to");

        // webApi Componentes
        Component ctrl = webApi.addComponent("Controllers", "Controllers Component");
        Component svc = webApi.addComponent("Services", "Services Component");
        Component rep = webApi.addComponent("Repository", "Repository Component");
        ctrl.uses(svc, "calls");
        svc.uses(rep, "calls");
        rep.uses(webDatabase, "calls");


        // bankaDesktop Componentes
        Component ctrlD = desktopApp.addComponent("Controllers", "Controllers Component");
        Component svcD = desktopApp.addComponent("Services", "Services Component");
        Component repD = desktopApp.addComponent("Repository", "Repository Component");
        ctrlD.uses(svcD, "calls");
        svcD.uses(repD, "calls");
        repD.uses(desktopDatabase, "calls");




        ViewSet viewSet = workspace.getViews();

        SystemContextView contextView = viewSet.createContextView(bankaWeb);
        contextView.addAllSoftwareSystems();
        contextView.addAllPeople();

        // and the container view
        ContainerView containerBankaWeb = viewSet.createContainerView(bankaWeb);
        containerBankaWeb.addAllPeople();
        containerBankaWeb.addAllContainers();

        ContainerView containerBankaDesktop = viewSet.createContainerView(bankaDesktop);
        containerBankaDesktop.addAllPeople();
        containerBankaDesktop.addAllContainers();

        ComponentView componentViewWebApi = viewSet.createComponentView(webApi);
        componentViewWebApi.setKey("components");        
        //componentViewWebApi.add(webApi);
        componentViewWebApi.addAllComponents();
        componentViewWebApi.add(webDatabase);


        ComponentView componentViewDesktopApp = viewSet.createComponentView(desktopApp);
        componentViewDesktopApp.setKey("components");        
        //componentViewDesktopApp.add(webApi);
        componentViewDesktopApp.addAllComponents();
        componentViewDesktopApp.add(desktopDatabase);

        // create your model and views here

        outputWorkspaceToConsole(workspace);
        uploadWorkspaceToStructurizr(workspace);
    }

    private static void outputWorkspaceToConsole(Workspace workspace) throws StructurizrWriterException {
        JsonWriter jsonWriter = new JsonWriter(true);
        StringWriter stringWriter = new StringWriter();
        jsonWriter.write(workspace, stringWriter);
        System.out.println(stringWriter.toString());
    }

    private static void uploadWorkspaceToStructurizr(Workspace workspace) throws Exception {
        StructurizrClient structurizrClient = new StructurizrClient("https://api.structurizr.com", "886bfd83-2a1a-4452-90ed-04b0ad0fdc07", "294b3215-2057-4786-9256-a8cf0eaddb56");
        //structurizrClient.putWorkspace(481, workspace);
        structurizrClient.mergeWorkspace(481, workspace);

        //mergeWorkspace
    }

}