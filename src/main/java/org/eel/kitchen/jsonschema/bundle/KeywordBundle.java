/*
 * Copyright (c) 2012, Francis Galiegue <fgaliegue@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.eel.kitchen.jsonschema.bundle;

import com.google.common.collect.ImmutableMap;
import org.eel.kitchen.jsonschema.main.JsonSchemaFactory;
import org.eel.kitchen.jsonschema.syntax.SyntaxChecker;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A keyword bundle
 *
 * <p>You can either create a new, completely empty, keyword bunle,
 * or use one of the default bundles and extend it. For instance:</p>
 *
 * <pre>
 *     final KeywordBundle bundle = KeywordBundles.defaultBundle();
 *
 *     final Keyword k1 = ...;
 *     final Keyword k2 = ...;
 *
 *     bundle.registerKeyword(k1);
 *     bundle.registerKeyword(k2);
 * </pre>
 *
 * @see Keyword
 * @see JsonSchemaFactory
 */
public final class KeywordBundle
    implements Iterable<Map.Entry<String, Keyword>>
{
    private final Map<String, Keyword> keywords
        = new HashMap<String, Keyword>();

    private final Map<String, SyntaxChecker> syntaxCheckers
        = new HashMap<String, SyntaxChecker>();

    /**
     * Package-private method to generate a full copy of this bundle
     *
     * @return an identical copy of this bundle
     */
    KeywordBundle copy()
    {
        final KeywordBundle ret = new KeywordBundle();
        ret.keywords.putAll(keywords);
        ret.syntaxCheckers.putAll(syntaxCheckers);
        return ret;
    }

    /**
     * Register a keyword for this bundle
     *
     * <p>Note that it is NOT allowed to register the same keyword twice. If
     * you must do so, you must call {@link #unregisterKeyword(String)} first.
     * </p>
     *
     * @see Keyword.Builder
     * @see Keyword
     *
     * @param keyword the keyword to register
     * @throws IllegalArgumentException a keyword by that name already exists
     */
    public void registerKeyword(final Keyword keyword)
    {
        final String name = keyword.getName();
        final SyntaxChecker checker = keyword.getSyntaxChecker();

        keywords.put(name, keyword);
        if (checker != null)
            syntaxCheckers.put(name, checker);
    }

    /**
     * Unregister a keyword
     *
     * @param name the name of the keyword to unregister
     */
    public void unregisterKeyword(final String name)
    {
        keywords.remove(name);
        syntaxCheckers.remove(name);
    }

    public Map<String, SyntaxChecker> getSyntaxCheckers()
    {
        return ImmutableMap.copyOf(syntaxCheckers);
    }

    /**
     * Iterator over an unmodifiable copy of registered keywords
     *
     * <p>Also used to implement {@link Iterable}</p>
     *
     * @return the iterator
     */
    @Override
    public Iterator<Map.Entry<String, Keyword>> iterator()
    {
        return ImmutableMap.copyOf(keywords).entrySet().iterator();
    }
}
