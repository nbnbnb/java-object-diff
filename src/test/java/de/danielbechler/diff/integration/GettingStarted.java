package de.danielbechler.diff.integration;

import de.danielbechler.diff.*;
import de.danielbechler.diff.node.*;
import de.danielbechler.diff.visitor.*;

/** @author Daniel Bechler */
public class GettingStarted
{
	private GettingStarted()
	{
	}

	public static void main(final String[] args)
	{
		helloWorldExample();
		phoneBookExample();
	}

	private static void helloWorldExample()
	{
		final ObjectDiffer objectDiffer = ObjectDifferFactory.getInstance();

		final String working = "Hello";
		final String base = "World";
		final Node root = objectDiffer.compare(working, base);

		root.visit(new PrintingVisitor(working, base));
	}

	private static void phoneBookExample()
	{
	final PhoneBook phoneBook = new PhoneBook("Breaking Bad");

	final Contact walterWhite = new Contact("Walter", "White");
	walterWhite.setPhoneNumber("Home", new PhoneNumber("1", "505", "316-7871"));
	walterWhite.setPhoneNumber("Work", new PhoneNumber("1", "505", "456-3788"));
	phoneBook.addContact(walterWhite);

	final Contact jessePinkman = new Contact("Jesse", "Pinkman");
	jessePinkman.setPhoneNumber("Home", new PhoneNumber("1", "505", "234-4628"));
	phoneBook.addContact(jessePinkman);

	final PhoneBook modifiedPhoneBook = PhoneBook.from(phoneBook);
	modifiedPhoneBook.getContact("Jesse", "Pinkman").setMiddleName("Bruce");
	modifiedPhoneBook.getContact("Walter", "White").setMiddleName("Hartwell");

	final ObjectDiffer objectDiffer = ObjectDifferFactory.getInstance();
	final Node root = objectDiffer.compare(modifiedPhoneBook, phoneBook);
	final Node.Visitor visitor = new PrintingVisitor(modifiedPhoneBook, phoneBook);
	root.visit(visitor);
	}
}
