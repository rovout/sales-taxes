package com.gcipriano.katas.salestaxes;

import com.gcipriano.katas.salestaxes.input.TextInput;
import com.gcipriano.katas.salestaxes.model.product.catalog.InMemoryExemptedProductCatalog;
import com.gcipriano.katas.salestaxes.model.taxing.InMemoryTaxFactory;
import com.gcipriano.katas.salestaxes.rounding.Nearest05RoundingPolicy;
import com.gcipriano.katas.salestaxes.receipt.BulletPointReceiptBuilder;
import com.gcipriano.katas.salestaxes.usecase.SalesTaxes;
import com.gcipriano.katas.salestaxes.model.ShoppingBasket;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AcceptanceTest
{
  private final InMemoryExemptedProductCatalog productCatalog = new InMemoryExemptedProductCatalog(asList(
      "book",
      "chocolate bar",
      "box of chocolates",
      "packet of headache pills"));
  private SalesTaxes salesTaxes;

  @Before
  public void setUp() throws Exception
  {
    salesTaxes = new SalesTaxes(new ShoppingBasket(new BulletPointReceiptBuilder(),
                                                   new InMemoryTaxFactory(productCatalog),
                                                   new Nearest05RoundingPolicy()));
  }

  @Test
  public void firstScenario() throws Exception
  {
    TextInput input = new TextInput("1 book at 12.49 "
                                        + "1 music CD at 14.99 "
                                        + "1 chocolate bar at 0.85 ");

    assertThat(salesTaxes.receipt(input), is("1 book: 12.49\n"
                                          + "1 music CD: 16.49\n"
                                          + "1 chocolate bar: 0.85\n"
                                          + "Sales Taxes: 1.50\n"
                                          + "Total: 29.83"));
  }

  @Test
  public void secondScenario() throws Exception
  {
    TextInput input = new TextInput("1 imported box of chocolates at 10.00 "
                                        + "1 imported bottle of perfume at 47.50");

    assertThat(salesTaxes.receipt(input), is("1 imported box of chocolates: 10.50\n"
                                          + "1 imported bottle of perfume: 54.65\n"
                                          + "Sales Taxes: 7.65\n"
                                          + "Total: 65.15"));

  }

  @Test
  public void thirdScenario() throws Exception
  {
    TextInput input = new TextInput("1 imported bottle of perfume at 27.99 "
                                        + "1 bottle of perfume at 18.99 "
                                        + "1 packet of headache pills at 9.75 "
                                        + "1 imported box of chocolates at 11.25"
    );

    assertThat(salesTaxes.receipt(input), is("1 imported bottle of perfume: 32.19\n"
                                          + "1 bottle of perfume: 20.89\n"
                                          + "1 packet of headache pills: 9.75\n"
                                          + "1 imported box of chocolates: 11.85\n"
                                          + "Sales Taxes: 6.70\n"
                                          + "Total: 74.68"));
  }
}
