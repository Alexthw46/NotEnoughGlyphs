package alexthw.not_enough_glyphs.common.glyphs.contingency;

import alexthw.not_enough_glyphs.common.glyphs.CompatRL;
import com.alexthw.sauce.common.glyphs.AbstractContingency;
import com.hollingsworth.arsnouveau.api.spell.IPotionEffect;

public abstract class NEGAbstractContingency extends AbstractContingency implements IPotionEffect {

    public NEGAbstractContingency(String effectName, String description) {
        super(CompatRL.neg(effectName), description);
    }

}
