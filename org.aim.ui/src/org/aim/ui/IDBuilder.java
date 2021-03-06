package org.aim.ui;

import java.util.Collection;

import org.aim.description.InstrumentationDescription;
import org.aim.description.builder.InstrumentationDescriptionBuilder;
import org.aim.description.builder.InstrumentationEntityBuilder;
import org.aim.description.builder.RestrictionBuilder;
import org.aim.description.restrictions.Restriction;
import org.aim.ui.bci.ScopePanel;
import org.aim.ui.entities.RawInstrumentationEntity;
import org.aim.ui.view.MainView;
import org.aim.ui.view.sampler.SamplerComponent;

/**
 * 
 * @author Marius Oehler
 *
 */
public final class IDBuilder {

	/**
	 * Builds an {@link InstrumentationDescription} from a collection of
	 * {@link RawInstrumentationEntity}.
	 * 
	 * @param instrumentationEntites
	 *            - collection of {@link RawInstrumentationEntity} which
	 *            represents {@link org.aim.description.InstrumentationEntity}
	 * @param globalRestriction
	 *            - the global restrictions
	 * @return an {@link InstrumentationDescription}
	 */
	public static InstrumentationDescription build(Collection<RawInstrumentationEntity> instrumentationEntites,
			Restriction globalRestriction) {
		InstrumentationDescriptionBuilder descBuilder = new InstrumentationDescriptionBuilder();

		for (RawInstrumentationEntity entity : instrumentationEntites) {
			InstrumentationEntityBuilder<?> entBuilder;

			// ###### Create Scope
			if (entity.getScope().equals(ScopePanel.METHOD_SCOPE)) {
				if (entity.isTraceScope()) {
					entBuilder = descBuilder.newTraceScopeEntity().setMethodSubScope(entity.getScopeSettings());
				} else {
					entBuilder = descBuilder.newMethodScopeEntity(entity.getScopeSettings());
				}
			} else if (entity.getScope().equals(ScopePanel.CONSTRUCTOR_SCOPE)) {
				if (entity.isTraceScope()) {
					entBuilder = descBuilder.newTraceScopeEntity().setConstructorSubScope(entity.getScopeSettings());
				} else {
					entBuilder = descBuilder.newConstructorScopeEntity(entity.getScopeSettings());
				}
			} else if (entity.getScope().equals(ScopePanel.ALLOCATION_SCOPE)) {
				entBuilder = descBuilder.newAllocationScopeEntity(entity.getScopeSettings());
			} else if (entity.getScope().equals(ScopePanel.MEMORY_SCOPE)) {
				entBuilder = descBuilder.newMemoryScopeEntity();
			} else if (entity.getScope().equals(ScopePanel.SYNCHRONIZED_SCOPE)) {
				entBuilder = descBuilder.newSynchronizedScopeEntity();
			} else {
				entBuilder = descBuilder.newAPIScopeEntity(entity.getScope());
			}

			// ###### Add Probes
			for (String probe : entity.getProbes()) {
				entBuilder.addProbe(probe);
			}

			// ###### Add Restrictions
			RestrictionBuilder<?> restrictionBuilder = entBuilder.newLocalRestriction();

			for (int modifier : entity.getExcludedModifiers()) {
				restrictionBuilder.excludeModifier(modifier);
			}
			for (int modifier : entity.getIncludedModifiers()) {
				restrictionBuilder.includeModifier(modifier);
			}

			for (String pge : entity.getExcludedPackages()) {
				restrictionBuilder.excludePackage(pge);
			}
			for (String pge : entity.getIncludedPackages()) {
				restrictionBuilder.includePackage(pge);
			}

			restrictionBuilder.restrictionDone();

			// ###### Done
			entBuilder.entityDone();
		}

		// ###### Sampler
		for (SamplerComponent comp : MainView.instance().getAllSamplerComponents()) {
			descBuilder.newSampling(comp.getSampler(), comp.getDelay());
		}

		// ###### Global Restriction
		RestrictionBuilder<InstrumentationDescriptionBuilder> globalRestrictionBuilder = descBuilder
				.newGlobalRestriction();

		for (int modifier : globalRestriction.getModifierExcludes()) {
			globalRestrictionBuilder.excludeModifier(modifier);
		}
		for (int modifier : globalRestriction.getModifierIncludes()) {
			globalRestrictionBuilder.includeModifier(modifier);
		}

		for (String pge : globalRestriction.getPackageExcludes()) {
			globalRestrictionBuilder.excludePackage(pge);
		}
		for (String pge : globalRestriction.getPackageIncludes()) {
			globalRestrictionBuilder.includePackage(pge);
		}

		globalRestrictionBuilder.restrictionDone();

		// ###### Build
		InstrumentationDescription instDescription = descBuilder.build();

		return instDescription;
	}

	/**
	 * Hidden constructor to prevent instantiation of this class.
	 */
	private IDBuilder() {
	}
}
