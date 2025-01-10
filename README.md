# Chinese Checkers (Chińskie Warcaby)

### Autorzy
Błażej Pawluk, Tomasz Stefaniak

### Intrukcja uruchomienia serwera i klienta
Projekt zawiera 3 pliki pom.xml z których ten znajdujący się bezpośrednio pod /chinesecheckers to projekt nadrzędny.  
Do budowania i uruchomienia można wykorzystać gotowe skrypty do shella pod /scripts albo oczywiście ręcznie wpisać komendy się w nich znajdujące.  
W przypadku uruchamiania skryptów cwd musi być ścieżką do repozytorium. Opis działania skryptów:  
> cp_all.sh (clean package) robi clean obu projektów podrzędnych (client, server) i wykonuje package  
> cp_client.sh robi clean i package tylko na projekcie podrzędnym klienta  
> cp_server.sh robi clean i package tylko na projekcie podrzędnym serwera  
> run_client.sh uruchamia klienta (oczywiście o ile wcześniej został wytworzony plik .jar)  
> run_server.sh uruchamia serwer  
