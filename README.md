Die Cloud basiert auf Nodes wobei ein node den Main node macht. 
Dieser node hat jedoch nicht mehr fähigkeiten als die anderen, 
sondern aktiviert nur seinen "Manager"-Mode um die Cloud zu steuern.
Das ermöglicht das dynamische auswählen und wechseln des Main Nodes. 
Wenn also der erste Node startet, ist dieser der Main Node und managt die Cloud.
Sobald ein weiterer Node gestartet wird, überprüft dieser,
ob es bereits einen MainNode gibt und falls ja, 
verbindet er sich einfach in das Netzwerk und sendet ein RegistrierungsPacket. 
Ab diesem Zeitpunkt bekommt er vom MainNode seine Aufgaben zugewiesen. 
Alle Nodes überprüfen in regelmäßigen Abständen, 
ob der MainNode noch existiert und falls dies 2-mal in Folge nicht der Fall ist, 
trägt sich der Node als MainNode ein, der den Ausfall zuerst bemerkt. 
Er sendet allen ein MainNodeChangePacket damit alle den Wechsel mitbekommen 
und wechselt in den ManagerMode. Alle Server, die zu diesem Zeitpunkt laufen,
werden nicht gestoppt aber im Hintergrund von dem neuen Node gesteuert.
Der aktuelle MainNode wird anhand seiner NetworkID, welche im die NetworkAPI zuteilt,
in einer RedisDatenbank gespeichert und muss regelmäßig nach getragen werden. Ist dies nicht der Fall,
wechselt der MainNode.

Wenn der Serverhoster eine API hat ist es möglich mehrere VServer zu nutzen und diese nur
dann zu starten, wenn sie gebraucht werden. Dabei würde ein Server permanent laufen
und ein anderer Server würde gestartet/gestoppt je nach cloud auslastung.

Wenn ein CloudNode disconnectet laufen die Server, welche von dem Node gesteuert wurden noch weiter,
es können jedoch keine neuen Server auf dem VServer des Nodes gestartet werden.