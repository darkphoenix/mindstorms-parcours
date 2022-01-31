# mindstorms-parcours

Aufbau des Roboters
Zentral und symmetrisch angeordnet sind zwei große Motoren, die als Antrieb dienen. Wir verwenden „Ketten“ – Bänder, die über die Reifen gespannt sind. Darauf befindet sich der EV3-Baustein. Vorne befindet sich der Farb-Sensor an einem Motor befestigt. Ebenfalls nach vorne zeigt der Ultraschallsensor zur Abstandsmessung zur Umgebung. Seitlich Am Roboter befestigt befindet sich der Gyro-Sensor. Nach hinten ausgerichtet hat der Roboter eine Schiebevorrichtung, welche am Berührungssensor befestigt ist.
Grundkonzept der einzelnen Parcours-Abschnitte:
Linienfolgen:
Der Roboter verwendet den Farbsensor, im „“-Modus und fährt mithilfe eines Reglers mit dem Sensor am Rande der Linie entlang. Wenn er die Linie verloren hat (aufgrund von Kurven oder Lücken), sucht er sie wieder, indem er mit dem kleinen Motor den Farbsensor nach rechts und links schwenkt. Wenn er die Linie dabei nicht wieder findet, hat er (wenn alles gut gelaufen ist) eine Lücke gefunden und fährt ein Stück geradeaus, bis er die Linie wieder gefunden hat. Ansonsten korrigiert er nach rechts bzw. links, bis er bestenfalls direkt wieder mit dem Regler weiterfahren kann.
Block umfahren:
Wenn der Roboter beim Linienfolgen ein Hindernis mit dem Ultraschallsensor unmittelbar vor sich erkennt, umfährt er mit einem festen Bewegungsablauf das Hindernis.
Box in die Ecke schieben:
Der Roboter fährt eine feste Distanz zur Box hin, bis er sich dann nach rechts dreht, bis er mit dem Ultraschallsensor die Box erkennt. Dann dreht er sich einmal um, sodass er die Box rückwärts mit der Schiebevorrichtung direkt diagonal über das Feld in die Ecke schieben kann.
Brücke überqueren:
Da der Roboter vom letzten Abschnitt noch in der Ecke mit der Box ist, muss er zunächst die Brücke finden. Dafür fährt der Roboter zunächst etwas aus der Ecke, dreht sich dann langsam um während der Drehung die Distanzen zur Wand zu messen. Auf die Art und Weise können wir die Lücke zwischen Brücke und Wand und die Wand links neben der Brücke erkennen. So kann der Roboter die Richtung anpassen und zur Brücke fahren.
Mit dem Gyrosensor erkennt der Roboter, wann er die Brücke hochfährt. Sobald das der Fall ist, verwendet er den Farbsensor, um mithilfe eines Reglers an der Kante links (der Farbsensor kann den Abgrund erkennen) die Brücke hochzufahren. Sobald der Roboter mithilfe des Gyrosensors erkennt, dass er oben auf der Brücke angekommen ist, fährt er so lange geradeaus, bis er mithilfe des Farbsensors den Abgrund vor sich erkennt. Dann dreht er sich nach links und überquert die Querstrecke oben auf der Brücke, bis er erneut einen Abgrund erkennt, um sich dann erneut nach links(?) zu drehen.
… runterfahren
Farbfelder finden:
Einzelne Termine
18.10.2021:
Bauen von Grundgerüst. Idee schwenkender Farbsensor und statt Reifen Ketten.
25.10.2021:
Fertigstellung von erstem Prototyp. Linienfolgen mit schwenkendem Sensor (Probleme, dass alle Ereignisse – Sensor schwenken und weiterfahren nacheinander passieren, also langsam). Dadurch, dass wir immer mit dem Sensor hin- und herschwenken, sind wir besonders auf geraden Streckenabschnitten sehr langsam. Details: wir messen einen Wert links und einen rechts
08.11.2021:
Umsetzung neuer Idee aus Kombination von schwenkendem Sensor und stillstehendem Sensor, damit wir bei geraden Strecken Zeit sparen können. Außerdem können wir jetzt parallel fahren und den Sensor verwenden.
15.11.2021:
Anwendung des neu auf dem Übungsblatt gelernten Reglers, damit wir gerade Strecken und Strecken mit weiten Kurven mit dem Regler fahren können und sobald wir die Linie verlieren, unseren schwenkenden Farbsensor Einsetzen. Wir verwenden jetzt beim Schwenken möglichst viele Messwerte statt nur zwei rechts und links.
22.11.2021:
29.11.2021:
Neuer Programmteil "HERMES" für den Abschnitt des Paketzustellens. Unser Roboter erkennt nun beim Linienfolgen den Block und kann ihn umfahren. Weitere Anpassungen am Linienfolger.
06.12.2021:
Anpassungen an HERMES und Neuasrichtung nachdem wir das Paket in die Ecke geschoben haben, damit wir die blaue Linie finden können.
13.12.2021:
parallele Arbeit an Paketzustellungs- und Brückenüberquerungsaufgabe (und Testen der Anpassungen). Erkennen von blauer Linie nach Paketzustellung. Kalibrierung von Weißwerten am Anfang des Parcours.
20.12.2021:
Roboter kann jetzt nach Paketzustellung jetzt die blaue Linie vor der Brücke finden, indem wir die Distanz zur Wand auf der anderen Seite messen und so lange nach links drehen, bis wir die Wand vor dem Brückenabschnitt sehen. Anpassung der Farberkennung für die blaue Linie, da wir auf dem Weg zur blauen Linie auch über weiße Linien fahren, die ebenfalls sehr hohe Blauwerte ausgeben auf dem Farbsensor (aber auch sehr hohe Rotwerte, im Gegensatz zur blauen Linie).
Bei der Brücke fahren wir geradeaus hoch. Solange wir schräg stehen (Gyrosensor!) ignorieren wir Abgründe, die wir mit dem Farbsensor sehen können (und vertrauen darauf, dass wir gerade genug ausgerichtet waren). Wenn wir oben auf der Brücke geradestehen, fahren wir so lange geradeaus, bis wir den Abgrund erreichen und biegen dann nach links ab.
10.01.2021:
Wir haben ein neues Framework und mussten einige neue Bugs, die dabei entstanden sind beheben.
Restliche Brückensegmente hinzugefügt, damit unser Roboter jetzt nach dem Hochfahren auch den Querabschnitt geradeausfährt und dann wieder links abbiegt und dann herunterfährt, allerdings verwenden wir aktuell nur beim Hochfahren einen Regler, sodass wir spätestens beim Herunterfahren der Brücke nicht durch die Lücke zum letzten Abschnitt kommen.
17.01.2021:
Unser Roboter kann jetzt beim Hochfahren der Brücke an der linken Kante entlangfahren mithilfe eines Reglers.
Unser Linienfolger hatte zuvor viele Fehler und war sehr langsam, daher haben wir angefangen, den Code nochmal grundlegend zu überarbeiten. Außerdem haben wir unsere Farbkalibrierung entfernt (ursprünglich war diese gedacht als Anpassung des Farbsensors an die jeweiligen Lichtverhältnisse), da diese beim Linienfolgen nicht geholfen hatte und bei jedem Parcours-Versuch nur andere Ergebnisse brachte.
Grundkonzept eines Suchmusters (Beschreibung) beim finden der Farbfelder im letzten Abschnitt
Roboter zeigt jetzt beim Programmstart an, welche Sensoren er gerade initialisiert (Gyro-, Farb- und Ultraschallsensor)
24.01.2021:
Bug in Rechtskurven behoben. Rechtskurven, die unser Roboter zuvor dadurch nicht richtig abgefahren ist (meistens ist er einfach geradeaus gefahren) fährt er jetzt gut ab.
Des Weiteren haben wir die Farberkennung für den letzten Abschnitt hinzugefügt und die Winkel der Kurven für das Abfahren des Feldes angepasst.
Unser Roboter piepst jetzt beim Übergang zwischen den Abschnitten.
Wir können jetzt während das Programm läuft auf unser internes Menü zugreifen, um Abschnitte erneut aufrufen zu können, ohne das Programm komplett neustarten zu müssen.
