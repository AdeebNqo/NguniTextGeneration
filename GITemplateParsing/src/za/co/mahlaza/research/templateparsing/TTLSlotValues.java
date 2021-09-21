package za.co.mahlaza.research.templateparsing;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;
import za.co.mahlaza.research.grammarengine.base.interfaces.SlotFiller;
import za.co.mahlaza.research.grammarengine.base.models.template.SlotValues;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TTLSlotValues extends SlotValues {

    private String baseURI;
    private String slotFillerFilename;
    List<SlotFiller> slotFillers;
    HashMap<String, SlotFiller> SV = new HashMap<>();

    public TTLSlotValues(HashMap<String, SlotFiller> fillers) {
        SV = fillers;
    }

    public TTLSlotValues(String filename, String baseURI) {
        slotFillers = TemplateReader.parseSlotFillers(filename);
        slotFillerFilename = filename;
        this.baseURI = baseURI;
    }

    public void shuffle() {
        Collections.shuffle(slotFillers);
    }

    public Model getUnderlyingModel() {
        Model model = ModelFactory.createDefaultModel();
        InputStream in = FileManager.get().open(slotFillerFilename);
        model.read(in, baseURI, "TTLSlotValues"); //TODO: move the TLL to the params so we cant handle diff. serializations
        return model;
    }

    public HashMap<String, SlotFiller> getValues () {
        if (SV.size() == 0 && slotFillers!= null ) {
            //TODO: remove this
            int maxFillsIn = getMaxSlotFillsIn(slotFillers);

            for (SlotFiller sf : slotFillers) {
                sf.shuffleFillsInLabels();
            }

            for (int i=0; i<maxFillsIn; i++) {
                for (SlotFiller slotFiller : slotFillers) {
                    if (slotFiller.getSlotsItCanFill().size() > i) {
                        String label = slotFiller.getSlotsItCanFill().get(i);
                        if (!SV.containsKey(label)) {
                            SV.put(label, slotFiller);
                        }
                    }
                }
            }
        }
        return SV;
    }

    public int getMaxSlotFillsIn(List<SlotFiller> slotFillers) {
        int max = -1;

        for (SlotFiller slotFiller : slotFillers) {
            if (max < slotFiller.getSlotsItCanFill().size()) {
                max = slotFiller.getSlotsItCanFill().size();
            }
        }

        return max;
    }
}
