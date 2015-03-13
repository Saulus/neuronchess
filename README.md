neuronchess
===========

The project allows
a) playing chess :-)
but b) first and foremost testing several machine learning methods, e.g. LogReg and (planned) deep neuronal nets, by letting them play against each other

Two views available: a gui, and text based.

Needs ND4J, and later deeplearning4j.

Start with: java -jar neuronchess.jar gui|text

German language (don't know why I decided to switch to German).


Methods implemented:
- Uniform (benchmark)
- simple LogReg


Open points:
- LogReg spielt schlecht
	 -> Test: korrekte Sortierung und Auswahl der Moves?
	 -> Test: Korrektes Lernen? (Mini Batch GD)
	 -> Aufteilen des Spiels in Anfang/Mitte/Schluss (3x Theta) anhand a)Zugzahl und b)Figurenzahl
- add further ML

