package com.dnarvaez27.pie_chart;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import com.dnarvaez27.pie_chart.recursos.ConstantesPieChart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

/**
 * Clase que modela un panel con una gráfica de pastel
 *
 * @see {@link javax.swing.JPanel}
 * @author d.narvaez11
 */
public class PieChart extends JPanel implements MouseMotionListener
{
	/**
	 * Clase Version 1. Construye el PieChart
	 *
	 * @author d.narvaez11
	 */
	private static class PieChartGraph
	{
		private double cantidadTotal;

		private double diametro;

		private Graphics2D graphics;

		private double lastEndPoint = 0;

		private ArrayList<Pieza> piezas;

		private double xCenter;

		private double yCenter;

		public PieChartGraph( Graphics g )
		{
			graphics = ( Graphics2D ) g;
			piezas = new ArrayList<>( );
		}

		public Pieza agregarPiezaCantidad( double pCantidad, Color bg, Color fg, String text )
		{
			double newEndPoint = ( ( pCantidad * 360 ) / cantidadTotal ) + lastEndPoint;

			graphics.setColor( bg );
			Pieza pieza = new Pieza( text, bg, fg, xCenter, yCenter, diametro, diametro, lastEndPoint, newEndPoint - lastEndPoint, Arc2D.PIE );
			graphics.fill( pieza );
			lastEndPoint = newEndPoint;

			piezas.add( pieza );

			return pieza;
		}

		public Pieza agregarPiezaCantidad( double pCantidad, Color color, String text )
		{
			double newEndPoint = ( ( pCantidad * 360 ) / cantidadTotal ) + lastEndPoint;

			graphics.setColor( color );
			Pieza pieza = new Pieza( text, color, xCenter, yCenter, diametro, diametro, lastEndPoint, newEndPoint - lastEndPoint, Arc2D.PIE );
			graphics.fill( pieza );
			lastEndPoint = newEndPoint;

			piezas.add( pieza );

			return pieza;
		}

		public Ellipse2D.Double dibujarCirculo( int pX, int pY, int pRadio, Color color, double cantidadTotal )
		{
			this.cantidadTotal = cantidadTotal;
			piezas = new ArrayList<>( );

			xCenter = pX;
			yCenter = pY;
			diametro = pRadio;

			Ellipse2D.Double e = new Ellipse2D.Double( xCenter, yCenter, diametro, diametro );

			graphics.setColor( color );
			graphics.fill( e );
			return e;
		}

		public void hidePopups( )
		{
			for( Pieza pieza : piezas )
			{
				pieza.hidePopup( );
			}
		}
	}

	/**
	 * Clase que modela una pieza/porcion de la gráfica
	 *
	 * @author d.narvaez11
	 */
	private static class Pieza extends Arc2D.Double
	{
		private static final long serialVersionUID = -8429774259707193751L;

		/**
		 * Background de la pieza y del Popup
		 */
		private Color bg;

		/**
		 * Foreground del Popup
		 */
		private Color fg;

		/**
		 * Popup de la pieza
		 */
		private Popup popup;

		/**
		 * Etiqueta de la pieza
		 */
		private String texto;

		/**
		 * @param texto Etiqueta de la pieza
		 * @param bg Background de la pieza
		 * @param fg Foreground del Popup de la pieza
		 * @param x Posición en x donde se ubicará el centro de la pieza
		 * @param y Posición en y donde se ubivará el centro de la pieza
		 * @param w Width de la pieza
		 * @param h Height de la pieza
		 * @param start Angulo de inicio de la pieza
		 * @param extent Angulo final de la pieza
		 * @param type Tipo de arco
		 */
		public Pieza( String texto, Color bg, Color fg, double x, double y, double w, double h, double start, double extent, int type )
		{
			super( x, y, w, h, start, extent, type );
			this.texto = texto;
			this.bg = bg;
			this.fg = fg;
		}

		/**
		 * Constructor de la Pieza
		 *
		 * @param texto Etiqueta de la pieza
		 * @param bg Background de la pieza
		 * @param x Posición en x donde se ubicará el centro de la pieza
		 * @param y Posición en y donde se ubivará el centro de la pieza
		 * @param w Width de la pieza
		 * @param h Height de la pieza
		 * @param start Angulo de inicio de la pieza
		 * @param extent Angulo final de la pieza
		 * @param type Tipo de arco
		 */
		public Pieza( String texto, Color bg, double x, double y, double w, double h, double start, double extent, int type )
		{
			super( x, y, w, h, start, extent, type );
			this.texto = texto;
			this.bg = bg;
		}

		/**
		 * Retorna el Color Background de la pieza
		 *
		 * @return Color background de la pieza
		 */
		public Color getBg( )
		{
			return bg;
		}

		/**
		 * Retorna el Color Foreground del Popup
		 *
		 * @return Color del Foreground del Popup de la pieza
		 */
		public Color getFg( )
		{
			return fg;
		}

		/**
		 * Retorna el Popup de la pieza
		 *
		 * @return Popup de la pieza
		 */
		public Popup getPopup( )
		{
			return popup;
		}

		/**
		 * Retorna la etiqueta de la pieza
		 *
		 * @return Etiqueta de la pieza
		 */
		public String getTexto( )
		{
			return texto;
		}

		/**
		 * Oculta el popup de la pieza
		 */
		public void hidePopup( )
		{
			if( popup != null )
			{
				popup.hide( );
			}
		}

		/**
		 * Configura el Popup de la pieza
		 *
		 * @param popup
		 */
		public void setPopup( Popup popup )
		{
			this.popup = popup;
		}
	}

	/**
	 * Modela el Listener para las porciones de la gráfica
	 *
	 * @author d.narvaez11
	 */
	private static class ShapeMouseListener implements MouseMotionListener
	{
		/**
		 * Pieza a la cual se le agregará el listener
		 */
		private Pieza pieza;

		/**
		 * Constructor del Listener
		 *
		 * @param pieza Pieza ala cual se le agregará el listener
		 */
		public ShapeMouseListener( Pieza pieza )
		{
			this.pieza = pieza;
		}

		/**
		 * Oculta el Popup de la pieza
		 */
		public void hidePopup( )
		{
			pieza.hidePopup( );
		}

		@Override
		public void mouseDragged( MouseEvent e )
		{
		}

		@Override
		public void mouseMoved( MouseEvent e )
		{
			if( pieza.contains( e.getPoint( ) ) )
			{
				hidePopup( );

				JPanel panelPop = new JPanel( );
				JLabel lab = new JLabel( pieza.getTexto( ) );
				if( pieza.getFg( ) != null )
				{
					lab.setForeground( pieza.getFg( ) );
				}

				panelPop.add( lab );
				panelPop.setBackground( pieza.getBg( ) );
				panelPop.setBorder( BorderFactory.createCompoundBorder( BorderFactory.createLineBorder( ConstantesPieChart.Colors.BLANCO ), BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) ) );

				pieza.setPopup( new PopupFactory( ).getPopup( panelPop, panelPop, e.getXOnScreen( ) + 10, e.getYOnScreen( ) + 10 ) );
				pieza.getPopup( ).show( );
			}
			else
			{
				hidePopup( );
			}
		}
	}

	/**
	 * Padding para el panel
	 */
	private static final int PADDING = 20;

	private static final long serialVersionUID = 1486880266466741986L;

	/**
	 * Cantidad Máxima del PieChart
	 */
	private double cantTotal;

	/**
	 * Color del background del PieChart
	 */
	private Color colorPie;

	/**
	 * Ellipse del PieChart
	 */
	private Ellipse2D.Double ellipse2d;

	/**
	 * Arreglo contenedor de los listeners
	 */
	private ArrayList<ShapeMouseListener> listeners;

	/**
	 * PieChartV1
	 */
	private PieChartGraph pieChartGraph;

	/**
	 * Arreglo de Piezas del PieChart
	 */
	private ArrayList<Pieza> piezas;

	/**
	 * Información de las piezas
	 */
	private ArrayList<Object[ ]> piezasInfo;

	/**
	 * Radio del PieChart
	 */
	private double radio;

	/**
	 * Constructor del Panel con el {@link PieChartGraph}
	 *
	 * @param diametro Diametro del PieChart
	 * @param cantTotal Cantidad total del PieChart
	 */
	public PieChart( double diametro, double cantTotal )
	{
		radio = diametro;
		this.cantTotal = cantTotal;

		listeners = new ArrayList<>( );
		piezas = new ArrayList<>( );
		piezasInfo = new ArrayList<>( );

		setPreferredSize( new Dimension( ( int ) diametro + PADDING, ( int ) diametro + PADDING ) );
		setBackground( null );
	}

	/**
	 * Constructor del Panel con el {@link PieChartGraph}
	 *
	 * @param diametro Diametro del PieChart
	 * @param total Cantidad total del PieChart
	 * @param color Color de Background del PieChart
	 */
	public PieChart( double diametro, double total, Color color )
	{
		radio = diametro;
		cantTotal = total;
		colorPie = color;

		listeners = new ArrayList<>( );
		piezas = new ArrayList<>( );
		piezasInfo = new ArrayList<>( );

		setPreferredSize( new Dimension( ( int ) diametro + PADDING, ( int ) diametro + PADDING ) );
		setBackground( null );
	}

	/**
	 * Reinicia y Actualiza el PieChart
	 *
	 * @param diametro Diametro del PieChart
	 * @param cantTotal Cantidad total de PieChart
	 */
	public void actualizar( double diametro, double cantTotal )
	{
		radio = diametro;
		this.cantTotal = cantTotal;

		removeListeners( );

		piezas = new ArrayList<>( );
		piezasInfo = new ArrayList<>( );
		listeners = new ArrayList<>( );

		setPreferredSize( new Dimension( ( int ) diametro + PADDING, ( int ) diametro + PADDING ) );
		setBackground( null );
	}

	/**
	 * Reinicia y actualiza el PieChart
	 *
	 * @param diametro Diametro del PieChart
	 * @param total Cantidad Total del PieChart
	 * @param color Background del PieChart
	 */
	public void actualizar( double diametro, double total, Color color )
	{
		radio = diametro;
		cantTotal = total;
		colorPie = color;

		removeListeners( );

		piezas = new ArrayList<>( );
		piezasInfo = new ArrayList<>( );
		listeners = new ArrayList<>( );

		setPreferredSize( new Dimension( ( int ) diametro + PADDING, ( int ) diametro + PADDING ) );
		setBackground( null );
	}

	/**
	 * Agrega una pieza al PieChart
	 *
	 * @param cantidad Cantidad de la pieza a agregar
	 * @param color Background de la pieza a agregar
	 * @param fg Foreground del Popup de la pieza a agregar
	 * @param text Etiqueta de la pieza a agregar
	 */
	public void agregarPieza( double cantidad, Color color, Color fg, String text )
	{
		Object[ ] items =
		{
				cantidad,
				color,
				fg,
				text,
		};

		piezasInfo.add( items );
	}

	/**
	 * Agrega una pieza al PieChart
	 *
	 * @param cantidad Cantidad de la pieza a agregar
	 * @param color Background de la pieza a agregar
	 * @param text Etiqueta de la pieza a agregar
	 */
	public void agregarPieza( double cantidad, Color color, String text )
	{
		Object[ ] items =
		{
				cantidad,
				color,
				text,
		};

		piezasInfo.add( items );
	}

	/**
	 * Esconde todos los Popups del PieChart
	 */
	public void hidePoups( )
	{
		pieChartGraph.hidePopups( );
		for( Pieza pieza : piezas )
		{
			pieza.hidePopup( );
		}
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		if( !ellipse2d.contains( e.getPoint( ) ) )
		{
			for( Pieza pieza : piezas )
			{
				pieza.hidePopup( );
			}
		}
	}

	@Override
	public void paint( Graphics g )
	{
		super.paint( g );

		removeMouseMotionListener( this );
		if( cantTotal != 0 )
		{
			addMouseMotionListener( this );
		}

		pieChartGraph = new PieChartGraph( g );

		int x = ( getX( ) + getWidth( ) ) / 2;
		int y = ( getY( ) + getHeight( ) ) / 2;

		if( getWidth( ) < radio )
		{
			double radioTemp = getWidth( );
			x = x - ( ( int ) radioTemp / 2 );
			y = y - ( ( int ) radioTemp / 2 );
			ellipse2d = pieChartGraph.dibujarCirculo( x, y, ( int ) radioTemp, colorPie != null ? colorPie : ConstantesPieChart.Colors.BLANCO, cantTotal );
		}
		else
		{
			x = ( int ) ( x - ( radio / 2 ) );
			y = ( int ) ( y - ( radio / 2 ) );
			ellipse2d = pieChartGraph.dibujarCirculo( x, y, ( int ) radio, colorPie != null ? colorPie : ConstantesPieChart.Colors.BLANCO, cantTotal );
		}

		for( Object[ ] obj : piezasInfo )
		{
			if( obj.length == 3 )
			{
				double cant = ( Double ) obj[ 0 ];
				Color color = ( Color ) obj[ 1 ];
				String text = ( String ) obj[ 2 ];

				Pieza pieza = pieChartGraph.agregarPiezaCantidad( cant, color, text );
				ShapeMouseListener shapeMouseListener = new ShapeMouseListener( pieza );
				listeners.add( shapeMouseListener );
				addMouseMotionListener( shapeMouseListener );
				piezas.add( pieza );
			}
			else
			{
				double cant = ( Double ) obj[ 0 ];
				Color color = ( Color ) obj[ 1 ];
				Color fg = ( Color ) obj[ 2 ];
				String text = ( String ) obj[ 3 ];

				Pieza pieza = pieChartGraph.agregarPiezaCantidad( cant, color, fg, text );
				ShapeMouseListener shapeMouseListener = new ShapeMouseListener( pieza );
				listeners.add( shapeMouseListener );
				addMouseMotionListener( shapeMouseListener );
				piezas.add( pieza );
			}
		}
	}

	/**
	 * Remueve todos los listeners del PieChart
	 */
	public void removeListeners( )
	{
		for( ShapeMouseListener shapeMouseListener : listeners )
		{
			removeMouseMotionListener( shapeMouseListener );
		}
	}
}