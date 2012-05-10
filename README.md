Transakční koordinace v JBoss ESB
====================


Obsah
---------------------

Tento repositář obsahuje zdrojové k diplomové práci na téma Transakční koordinace v JBoss ESB

Ve složce technicka_zprava je umístěna technická zpráva a zdrojové kódy potřebné pro vysázení. Práce je napsána v sázecím systému Latex.
Přiložen je makefile, který celou technickou zprávu vysází do souboru xzelin15.pdf

Ve složce program jsou umístěny zdrojové kódy k demonstrační aplikaci. Jedná se o webovou aplikaci napsanou v jazyce Java, která demonstruje použití transakčního zpracování v JBoss ESB. Aplikace představuje jednoduchý rezervační systém, který využívá webové služby. Při spuštění transakce se spustí rozhraní jednotlivých služeb, které byly vyvolány během transakce a lze sledovat průběh transakčního zpracování. 

Cílem této práce bylo vytvořit mechanizmus koordinace transakce, což zahrnuje připojení zdrojů do aktivní transakce a provedení dvoufázového commit protokolu.
  


