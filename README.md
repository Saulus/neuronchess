neuronchess
===========

ToDo:
- LogReg ist schlechter bei Schwarz -> korrekte Sortierung und Auswahl der Moves? Korrektes Lernen?
- Parameter besser einstellen
- Parallelisierung (Moves prüfen)
- Fortschrittsanzeige statt Spiel-Nr. anzeigen: 

 public class BackSpaceCharacterTest
{
    // the exception comes from the use of accessing the main thread
    public static void main(String[] args) throws InterruptedException
    {
        /*
            Notice the user of print as opposed to println:
            the '\b' char cannot go over the new line char.
        */
        System.out.print("Start[          ]");
        System.out.flush(); // the flush method prints it to the screen

        // 11 '\b' chars: 1 for the ']', the rest are for the spaces
        System.out.print("\b\b\b\b\b\b\b\b\b\b\b");
        System.out.flush();
        Thread.sleep(500); // just to make it easy to see the changes

        for(int i = 0; i < 10; i++)
        {
            System.out.print("."); //overwrites a space
            System.out.flush();
            Thread.sleep(100);
        }

        System.out.print("] Done\n"); //overwrites the ']' + adds chars
        System.out.flush();
    }
}