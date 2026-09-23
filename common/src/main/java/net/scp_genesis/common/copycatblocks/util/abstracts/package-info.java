/**
 * Vanilla-only helpers behind the Copycat abstract blocks.
 *
 * <p>Material, removal and interaction helpers preserve subclass callbacks instead of
 * exposing protected hooks. Half families implement {@link
 * net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfBehavior} and supply it
 * with a form to {@link AbstractCopycatHalfBlock}.
 * A new family defines its geometry, targeting, construction item and reversible merges;
 * slope renderers remain restricted to the slope behavior.</p>
 *
 * <p>The package is named {@code abstracts} because {@code abstract} is a reserved Java keyword.</p>
 */
package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock;