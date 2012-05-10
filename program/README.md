Demonstrační aplikace
====================

Demonstrační aplikace je vytvořena ze součástí XTS demo aplikace. Převzaty jsou webové služby a grafický návrh webové aplikace

Web klient
---------------------

Klient je JSP/Servlet aplikace, kde business logika je umístěna v ClientServlet.java. Zde se přijmou data z html formuláře, předají se do ESB zprávy a ta je odeslána do ESB aplikace.

Tělo zprávy obsahuje data pro vyvolání webových služeb. Pro každou webovou službu je do zprávy umístěna hash mapa s umístěním. Hash mapa obsahuje data ve formátu klíč-data. Klič je název metody webové služby.
Pokud není třeba službu vyvolávat je do ní umístěna hash mapa s obsahem delist-delist. Hodnoty delist-delist SOAP klient vyhodnotí a nevyvolá službu a posune zprávu dále po řetězení. Ve zprávě jsou také obsaženy informace o druhu transakce (atomické, business aktivity) a příkazu, který se má na konci transakce vykonat(commit-rollback/complete-cancel-close). 

Jelikož je celá aplikace asynchronní, výsledek transakce(úspěch, chyba, rollback) je zapsán do souboru. Obsah souboru je dostupný pomocí javascript ajax, odkazem Get result - viz ajax.js. Zápis do souboru je zvolen pouze kvůli snadnému nasazení pro demonstraci(odpadá vytváření databáze)


ESB část
---------------------

Koordinační služba je umístěna v balíčku ServiceConsumer. Konzument je jedna služba, která má za úkol spustit transakci, připojit zdroje a ukončit transakci. Transakce se spustí v akci MyRequestAction, kde je ze zprávy vyhodnoceno, který druh transakce se má spustit. 

Následně jsou vyvolávány služby, které se při vyvolání připojí k transakci. Poslední akce MyResponseAction vykonává ukončující příkazy transakce, které jsou obsaženy ve zprávě.

Vyvolání služby je provedeno pomocí wiseSOAPClient akce, která byla upravena tak, aby dokázala vyhodnotit delist-delist informace, viz třída UpgradedSOAPClient. Tento SOAP klient byl vybrán, protože je zde možnost umístit vlastní soap handler, který vloží transakční kontext do SOAP hlavičky, a tak zajistí jeho propagaci. Před každou akci SOAP klienta je předsazena akce DataBridge, která vyjme data z daného umístění v těle a jsou umístěna do standardního umístění (default location). Tak je zaručeno, že požadované data si "vyzvedne" požadovaná služba. Případně je možno data bridge upravit pro umístění odpovědi do zprávy.  

Poznámka: Data bridge bude možná nahrazeno smooks transformací - nedostatek informací/času.

Webové služby
---------------------

Pro zatím jsou do aplikace zahrnuty pouze služby pro atomické transakce. Služby obsahují registraci pro recovery službu i swing rozhraní pro sledování průběhu transakce, které se spustí s vyvoláním služby.
