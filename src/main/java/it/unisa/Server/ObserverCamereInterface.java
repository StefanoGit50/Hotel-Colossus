package it.unisa.Server;

import it.unisa.Common.Camera;
import it.unisa.Server.persistent.obj.catalogues.CatalogoCamerePublisher;

public interface ObserverCamereInterface {
    void attach(CatalogoCamerePublisher observer);
    void detach(CatalogoCamerePublisher observer);
    void notifyObservers();
}
