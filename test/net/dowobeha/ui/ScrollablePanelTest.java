package net.dowobeha.ui;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ScrollablePanelTest {

	@Test
	public void getNumComponentsToScrollDownSingleTest() {
		
		int numComponents = 1;
		
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  0), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  1), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  2), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  3), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  4), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  5), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  6), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  7), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  8), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  9), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents, 10), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents, 11), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents, 12), 1);
		
	}
	
	@Test
	public void getNumComponentsToScrollDownBlockTest() {
		
		int numComponents = 4;
		
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  0), 4);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  1), 3);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  2), 2);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  3), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  4), 4);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  5), 3);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  6), 2);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  7), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  8), 4);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents,  9), 3);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents, 10), 2);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents, 11), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollDown(numComponents, 12), 4);
		
	}
	
	
	@Test
	public void getNumComponentsToScrollUpSingleTest() {
		
		int numComponents = 1;
		
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  0), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  1), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  2), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  3), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  4), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  5), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  6), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  7), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  8), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  9), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents, 10), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents, 11), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents, 12), 1);
		
	}
	
	@Test
	public void getNumComponentsToScrollUpBlockTest() {
		
		int numComponents = 4;
		
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  0), 4);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  1), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  2), 2);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  3), 3);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  4), 4);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  5), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  6), 2);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  7), 3);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  8), 4);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents,  9), 1);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents, 10), 2);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents, 11), 3);
		Assert.assertEquals(ScrollablePanel.getNumComponentsToScrollUp(numComponents, 12), 4);
		
	}
	
}
