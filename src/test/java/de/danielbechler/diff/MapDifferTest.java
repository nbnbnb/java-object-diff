/*
 * Copyright 2012 Daniel Bechler
 *
 * This file is part of java-object-diff.
 *
 * java-object-diff is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * java-object-diff is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with java-object-diff.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.danielbechler.diff;

import de.danielbechler.diff.node.*;
import org.junit.*;

import java.util.*;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;
import static org.hamcrest.core.IsNull.*;
import static org.junit.Assert.*;

/** @author Daniel Bechler */
public class MapDifferTest
{
	private MapDiffer differ;

	@Before
	public void setUp()
	{
		differ = new MapDiffer();
	}

	@Test
	public void testWithAdditionOfSimpleTypeToWorkingMap()
	{
		final Map<String, String> base = new TreeMap<String, String>();
		final Map<String, String> working = new TreeMap<String, String>();
		working.put("foo", "bar");

		final MapNode node = differ.compare(working, base);
		assertThat(node.isMapDifference(), is(true));
		assertThat(node.hasChildren(), is(true));
		assertThat(node.getState(), is(Node.State.CHANGED));

		final Collection<Node> children = node.getChildren();
		assertThat(children.size(), is(1));

		final Node child = children.iterator().next();
		assertThat((String) child.get(working), equalTo("bar"));
		assertThat(child.get(base), nullValue());
		assertThat(child.getState(), is(Node.State.ADDED));
	}

	@Test
	public void testWithNewMapInWorkingAndNoneInBase()
	{
		final Map<String, String> base = null;
		final Map<String, String> working = new TreeMap<String, String>();
		working.put("foo", "bar");

		final MapNode node = differ.compare(working, base);
		assertThat(node.getState(), is(Node.State.ADDED));

		final Collection<Node> children = node.getChildren();
		assertThat(children.size(), is(1));

		final Node child = children.iterator().next();
		assertThat(child.getState(), is(Node.State.ADDED));
	}

	@Test
	public void testWithSameEntryInBaseAndWorking()
	{
		final Map<String, String> base = new TreeMap<String, String>();
		base.put("foo", "bar");
		final Map<String, String> working = new TreeMap<String, String>();
		working.put("foo", "bar");

		final MapNode node = differ.compare(working, base);
		assertThat(node.getState(), is(Node.State.UNTOUCHED));
		assertThat(node.hasChildren(), is(false));
	}

	@Test
	public void testWithSingleEntryAddedToWorkingMap()
	{
		final Map<String, String> base = new TreeMap<String, String>();
		base.put("foo", "bar");
		final Map<String, String> working = null;

		final MapNode node = differ.compare(working, base);
		assertThat(node.getState(), is(Node.State.REMOVED));

		final Collection<Node> children = node.getChildren();
		assertThat(children.size(), is(1));

		final Node child = children.iterator().next();
		assertThat(child.getState(), is(Node.State.REMOVED));
	}

	@Test
	public void testWithoutMapInBaseAndWorking()
	{
		final MapNode node = differ.compare((Map<?, ?>) null, null);
		assertThat(node.getState(), is(Node.State.UNTOUCHED));
		assertThat(node.hasChildren(), is(false));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructionWithoutDelegator()
	{
		new MapDiffer(null);
	}

	@Test
	public void testConstructionWithDelegator()
	{
		// just for the coverage
		new MapDiffer(new DelegatingObjectDiffer());
	}
}